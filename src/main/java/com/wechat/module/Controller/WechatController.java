package com.wechat.module.Controller;

import com.wechat.module.jsapi_ticket.utils.JsapiTicketUtil;
import com.wechat.module.utils.WechatUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Created by CAI_GC on 2016/11/28.
 */
@Controller
@Scope("prototype")
public class WechatController {


    private final Logger logger = LoggerFactory.getLogger(WechatController.class);

    private static final String TOKEN = "qywenji";

    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public void verification(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response) throws IOException {
        logger.info("signature:{}", signature);
        // 时间戳
        logger.info("timestamp:{}", timestamp);
        // 随机数
        logger.info("nonce:{}", nonce);
        // 随机字符串
        logger.info("echostr:{}", echostr);
        PrintWriter writer = response.getWriter();
        if (this.checkSignature(signature, timestamp, nonce)) {// 验证成功返回ehcostr
            writer.print(echostr);
        } else {
            writer.print("error");
        }
        writer.flush();
        writer.close();
    }

    private Boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] values = {TOKEN, timestamp, nonce};
        Arrays.sort(values); // 字典序排序
        String value = values[0] + values[1] + values[2];
        String sign = DigestUtils.sha1Hex(value);
        if (signature.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 页面签名函数，目的调用微信JS-API
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/sign", method = RequestMethod.GET)
    @ResponseBody
    private Object sign(String url) {
        /**
         * 跨域设置(如果需要)
         */
        /*response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");*/
        return WechatUtils.signForWebPage(url, JsapiTicketUtil.getJsapiTicket().getTicket());
    }
}
