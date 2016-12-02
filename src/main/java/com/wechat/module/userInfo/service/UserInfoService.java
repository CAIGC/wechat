package com.wechat.module.userInfo.service;

import com.alibaba.fastjson.JSONObject;
import com.wechat.module.access_token.utils.AccessTokenUtil;
import com.wechat.module.service.WechatService;
import com.wechat.module.userInfo.bean.UserInfo;
import com.wechat.module.utils.WechatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CAI_GC on 2016/11/29.
 */
@Service
public class UserInfoService {

    private final Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    /**
     * 关注后，获取用户基本信息接口
     */
    private static final String GET_USER_INFO_BY_OPENID = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";

    /**
     * 通过微信网页授权机制，来获取用户基本信息接口
     */
    private static final String GET_USER_INFO_BY_OAUTH2 = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";


    @Autowired
    private WechatService wechatService;

    /**
     * 已关注用户，获取用户基本信息
     *
     * @param openId
     * @return
     */
    public UserInfo getUserInfo(String openId) {
        String url = String.format(GET_USER_INFO_BY_OPENID, AccessTokenUtil.getAccessToken().getAccessToken(), openId);
        JSONObject jsonObject = wechatService.getJson(url, "获取关注用户基本信息失败");
        if (jsonObject == null) {
            return null;
        }
        return JSONObject.parseObject(jsonObject.toString(), UserInfo.class);
    }

    /**
     * 通过微信网页授权机制，来获取用户基本信息
     *
     * @param accessToken 通过code换取的是一个特殊的网页授权access_token，非全局accessToken
     * @param openId
     * @return
     */
    public UserInfo getUserInfo(String accessToken, String openId) {
        String url = String.format(GET_USER_INFO_BY_OAUTH2, accessToken, openId);
        JSONObject jsonObject = wechatService.getJson(url, "网页授权机制,获取用户基本信息失败");
        if (jsonObject == null) {
            return null;
        }
        return JSONObject.parseObject(jsonObject.toString(), UserInfo.class);
    }


    /**
     * 发送到用户系统
     * @param openId
     */
    public void sentUserInfo(final String  openId){
        new Thread(){
            public void run(){
                UserInfo userInfo = getUserInfo(openId);
                Map<String,String> postData = new HashMap<String, String>();
                // TODO
            }
        }.start();
    }
}
