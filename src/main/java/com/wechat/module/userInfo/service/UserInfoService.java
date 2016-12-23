package com.wechat.module.userInfo.service;

import com.alibaba.fastjson.JSONObject;
import com.wechat.module.access_token.utils.AccessTokenUtil;
import com.wechat.module.mq.WechatQueueMessageProducer;
import com.wechat.module.service.WechatService;
import com.wechat.module.userInfo.bean.UserInfo;
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
        Integer subscribe = jsonObject.getInteger("subscribe");
        String nickname = jsonObject.getString("nickname");
        Integer sex = jsonObject.getInteger("sex");
        String city = jsonObject.getString("city");
        String province = jsonObject.getString("province");
        String country = jsonObject.getString("country");
        Long subscribeTime = jsonObject.getLong("subscribe_time");
        String headImgUrl = jsonObject.getString("headimgurl");
        UserInfo userInfo = new UserInfo(subscribe,openId,nickname,sex,city,province,country,subscribeTime,headImgUrl);
        return userInfo;
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
        String nickname = jsonObject.getString("nickname");
        Integer sex = jsonObject.getInteger("sex");
        String city = jsonObject.getString("city");
        String province = jsonObject.getString("province");
        String country = jsonObject.getString("country");
        String headImgUrl = jsonObject.getString("headimgurl");
        UserInfo userInfo = new UserInfo(0,openId,nickname,sex,city,province,country,0L,headImgUrl);
        return userInfo;
    }


    /**
     * 关注
     * @param openId
     */
    public void subscribeSendMq(final String  openId){
        new Thread(){
            public void run(){
                UserInfo userInfo = getUserInfo(openId);
                if(userInfo != null){
                    Map<String,Object> mqData = new HashMap<>();
                    mqData.put("type","subscribe");
                    mqData.put("data",JSONObject.toJSONString(userInfo));
                    WechatQueueMessageProducer.send(mqData);
                }
            }
        }.start();
    }

    /**
     * 取消关注，发送mq
     * @param openId
     */
    public void cancelSubscribeSendMq(String openId){
        Map<String,Object> mqData = new HashMap<>();
        mqData.put("type","cancelSubscribe");
        mqData.put("data",openId);
        WechatQueueMessageProducer.send(mqData);
    }
}
