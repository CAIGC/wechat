package com.wechat.module.service;

import com.alibaba.fastjson.JSONObject;
import com.wechat.module.constant.WechatConfig;
import com.wechat.module.utils.WechatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by CAI_GC on 2016/11/29.
 */
@Service
public class WechatService {

    private final Logger logger = LoggerFactory.getLogger(WechatService.class);

    private static final String WX_GET_BASE_CODE_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";

    private static final String WX_GET_OAUTH_ACCESSTOKEN_URL="https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";



    public String getCodeUrl(String tourl,String snsapiFlag){
        String url = null;
        try {
            url = String.format(WX_GET_BASE_CODE_URL,
                    WechatConfig.APPID, URLEncoder.encode(tourl, "UTF-8"),snsapiFlag,"STATE");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 通过code换取网页授权access_token
     * @param code
     * @return
     */
    public JSONObject getOauthAccessTokenJsonByCode(String code){
        String url = String.format(WX_GET_OAUTH_ACCESSTOKEN_URL,WechatConfig.APPID,WechatConfig.APPSECRET,WX_GET_OAUTH_ACCESSTOKEN_URL,code);
        JSONObject jsonObject = this.getJson(url,"通过code换取网页授权access_token失败");
        if(null == jsonObject){
            return null;
        }
        return jsonObject;
    }

    /**
     * 微信接口（get方法）返回Json（非xml）数据统一处理
     * @param url
     * @param errMsgDec
     * @return
     */
    public JSONObject getJson(String url,String errMsgDec){
        JSONObject jsonObject = WechatUtils.get(url);
        if(jsonObject == null){
            logger.info(errMsgDec);
            return null;
        }
        if(jsonObject.getString("errmsg") != null){
            logger.info(errMsgDec+"errcode:{}, errmsg:{}",jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
            return null;
        }
        return jsonObject;
    }
}
