package com.wechat.module.Controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Created by CAI_GC on 2016/11/30.
 */
@Controller
@Scope("prototype")
public class WechatVerificationController {

    private final Logger logger = LoggerFactory.getLogger(WechatVerificationController.class);

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


    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public void response(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；
        // 调用核心业务类接收消息、处理消息
//        String respMessage = wxService.processRequest(request);
//        // 响应消息
//        if(respMessage != null){
//            PrintWriter out = response.getWriter();
//            out.print(respMessage);
//            out.close();
//        }


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
}
