package com.wechat.module.access_token.utils;

import com.alibaba.fastjson.JSONObject;
import com.wechat.commons.utils.RedisUtil;
import com.wechat.module.access_token.bean.AccessToken;
import com.wechat.module.constant.WechatConfig;
import com.wechat.module.utils.WechatUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by CAI_GC on 2016/11/28.
 */
public class AccessTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenUtil.class);

    private static final String ACCESS_TOKEN_KEY="WECHAT_WHOLE_ACCESS_TOKEN_KEY_";

    // 获取access_token的接口地址（GET） 限200（次/天）
    public final static String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    private static AccessToken accessToken;

    /*public static AccessToken getAccessToken() {
        if (accessToken == null) {
            synchronized (AcessTokenUtil.class) {
                if (accessToken == null) {
                    accessToken = new AccessToken();
                }
            }
        }
        return accessToken;
    }*/

    /**
     * 获取accessToken，有并发问题，但影响不大
     * @return
     */
    public static AccessToken getAccessToken() {
        String tokenStr = RedisUtil.get(ACCESS_TOKEN_KEY);
        if(StringUtils.isNotBlank(tokenStr)){
            return JSONObject.parseObject(tokenStr,AccessToken.class);
        }
        accessToken = getFromWechatServer();
        if(accessToken != null){
            RedisUtil.set(ACCESS_TOKEN_KEY,JSONObject.toJSONString(accessToken),accessToken.getExpiresIn()-60*10);//缓存110分钟
        }
        return accessToken;
    }

    private static AccessToken getFromWechatServer(String appId, String appsecret) {
        String url = String.format(GET_ACCESS_TOKEN_URL, appId, appsecret);
        JSONObject jsonObject = WechatUtils.get(url);
        if(jsonObject == null){
            logger.info("****获取token失败，微信返回结果为null");
            return null;
        }
        if (jsonObject.getIntValue("errcode") != 0) {
            logger.info("****获取token失败 errcode:{}, errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
            return null;
        }
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(jsonObject.getString("access_token"));
        accessToken.setExpiresIn(jsonObject.getInteger("expires_in"));
        return accessToken;
    }

    public static AccessToken getFromWechatServer(){
        return getFromWechatServer(WechatConfig.APPID,WechatConfig.APPSECRET);
    }
}
