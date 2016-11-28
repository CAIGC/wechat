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
 * Created by CAI_GC on 2016/11/28.
 */
@Controller
@Scope("prototype")
public class WechatController {


    private final Logger logger = LoggerFactory.getLogger(WechatController.class);

    private static final String TOKEN = "qywenji";

    @RequestMapping(value = "/weixin", method = RequestMethod.GET)
    public void valid(String signature,String timestamp,String nonce,String echostr,HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        logger.info("signature:{}", signature);
        // 时间戳
        logger.info("timestamp:{}", timestamp);
        // 随机数
        logger.info("nonce:{}", nonce);
        // 随机字符串
        logger.info("echostr:{}", echostr);
        PrintWriter writer = response.getWriter();
        if (checkSignature(signature, timestamp, nonce)) {// 验证成功返回ehcostr
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
}
