package com.wechat.module.Controller;

import com.wechat.module.utils.WechatUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.SortedMap;

/**
 * Created by CAI_GC on 2016/11/29.
 */
@Controller
@Scope("prototype")
public class PayCallbackController {

    private static final Logger logger = LoggerFactory.getLogger("wechatPay");

    private static final String SUCCESS_STATUS="SUCCESS";


    /**
     * 微信支付回调 参考 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
     * @param request
     * @param response
     * @throws java.io.IOException
     */
    @RequestMapping(value = "payCallbackHandle", method = RequestMethod.POST)
    public void payCallbackHandle(HttpServletRequest request, HttpServletResponse response)throws IOException{
        String result = this.getStringFromRequest(request);
        if(StringUtils.isBlank(result)){
            logger.info("微信返回结果为null");
            response.getWriter().write(this.setXML("FAIL", "签名验证失败"));
            return;
        }
        SortedMap<String, String> resultMap = WechatUtils.xmlStrToMap(result);
        if(SUCCESS_STATUS.equalsIgnoreCase(resultMap.get("return_code").toString())){
            String sign = resultMap.get("sign");
            String acSign = WechatUtils.createSign(resultMap);
            if (!sign.equals(acSign)) {
                logger.info("签名不一致,微信结果result:{}",result);
                response.getWriter().write(this.setXML("FAIL", "签名验证失败"));
                return;
            }
            String transaction_id = resultMap.get("transaction_id");//微信支付订单号
            String orderCode = resultMap.get("attach");
            if (StringUtils.isBlank(transaction_id) || StringUtils.isBlank(orderCode)) {
                logger.info("***********orderCode:{},transaction_id:{}", orderCode, transaction_id);
                response.getWriter().write(this.setXML("FAIL", "transaction_id为空"));
                return;
            }



            response.getWriter().write(this.setXML("SUCCESS", "OK"));
        }else{
            logger.info("get pay result fail,return_msg:{}",resultMap.get("return_msg"));
            response.getWriter().write(this.setXML("FAIL", "通讯失败"));
            return;
        }
    }


    private String getStringFromRequest(HttpServletRequest request){
        StringBuffer buffer = new StringBuffer();
        try {
            InputStream inputStream = request.getInputStream();
            // 将返回的输入流转换成字符串
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return buffer.toString();
    }

    private String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code
                + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }
}
