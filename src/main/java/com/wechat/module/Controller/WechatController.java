package com.wechat.module.Controller;

import com.alibaba.fastjson.JSONObject;
import com.wechat.commons.controller.BaseController;
import com.wechat.module.access_token.utils.AccessTokenUtil;
import com.wechat.module.jsapi_ticket.utils.JsapiTicketUtil;
import com.wechat.module.mq.WechatQueueMessageProducer;
import com.wechat.module.service.WechatService;
import com.wechat.module.userInfo.bean.UserInfo;
import com.wechat.module.userInfo.service.UserInfoService;
import com.wechat.module.utils.WechatUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Created by CAI_GC on 2016/11/28.
 */
@RestController
@Scope("prototype")
public class WechatController extends BaseController {


    private final Logger logger = LoggerFactory.getLogger(WechatController.class);
    private static final String SNSAPI_BASE="snsapi_base";
    private static final String SNSAPI_USERINFO="snsapi_userinfo";

    @Autowired
    private WechatService wechatService;

    @Autowired
    private UserInfoService userInfoService;
    /**
     * 页面签名函数，目的调用微信JS-API
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/sign", method = RequestMethod.GET)
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

    /**
     * 正确时返回的JSON数据包如下
     * {
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE",
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/getOauthAccessTokenJson", method = RequestMethod.POST)
    public Object getOauthAccessTokenJsonByCode(String code) {
        JSONObject jsonObject = wechatService.getOauthAccessTokenJsonByCode(code);
        if (jsonObject == null) {
            return super.error("失败");
        }
        return super.success(jsonObject);
    }

    /**
     * 获取关注用户的基本信息
     * @param openId
     * @return
     */
    @RequestMapping(value = "/getSubscribeUserInfo", method = RequestMethod.POST)
    public Object getSubscribeUserInfo(String openId){
        UserInfo userInfo = userInfoService.getUserInfo(openId);
        if(userInfo == null){
            return super.error("获取失败");
        }
        return super.success(userInfo);
    }

    /**
     * 通过微信网页授权机制，来获取用户基本信息
     * @param code
     * @return
     */
    @RequestMapping(value = "/getNotSubscribeUserInfo", method = RequestMethod.POST)
    public Object getNotSubscribeUserInfo(String code){
        JSONObject jsonObject = wechatService.getOauthAccessTokenJsonByCode(code);
        if(jsonObject == null){
            return super.error("获取网页授权accessToken信息失败");
        }
        UserInfo userInfo = userInfoService.getUserInfo(jsonObject.getString("access_token"),jsonObject.getString("openid"));
        if(userInfo == null){
            return super.error("获取用户基本信息失败");
        }
        return super.success(userInfo);
    }


    @RequestMapping(value = "/getBaseCode", method = RequestMethod.POST)
    public void getBaseCodeUrl(String redirect_url,HttpServletResponse response)throws Exception{
        response.sendRedirect(wechatService.getCodeUrl(redirect_url,SNSAPI_BASE));
    }

    @RequestMapping(value = "/getUserInfoCode", method = RequestMethod.POST)
    public void getUserInfoCodeUrl(String tourl,HttpServletResponse response)throws Exception{
        response.sendRedirect(wechatService.getCodeUrl(tourl,SNSAPI_USERINFO));

    }
    @RequestMapping(value = "/getAccessToken")
    public Object getAccessToken(){
        return super.success(AccessTokenUtil.getAccessToken().getAccessToken());
    }

}
