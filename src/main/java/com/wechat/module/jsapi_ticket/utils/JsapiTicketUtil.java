package com.wechat.module.jsapi_ticket.utils;

import com.alibaba.fastjson.JSONObject;
import com.wechat.commons.utils.RedisUtil;
import com.wechat.module.access_token.bean.AccessToken;
import com.wechat.module.access_token.utils.AccessTokenUtil;
import com.wechat.module.jsapi_ticket.bean.JsapiTicket;
import com.wechat.module.utils.WechatUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by CAI_GC on 2016/11/28.
 */
public class JsapiTicketUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsapiTicketUtil.class);

    private static final String JSAPI_TICKET_KEY="WECHAT_JSAPI_TICKET_KEY_";

    private static final String GET_JAAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

    private static JsapiTicket jsapiTicket;

    public static JsapiTicket getJsapiTicket(){
        String ticketStr = RedisUtil.get(JSAPI_TICKET_KEY);
        if(StringUtils.isNotBlank(ticketStr)){
            return JSONObject.parseObject(ticketStr, JsapiTicket.class);
        }
        AccessToken accessToken = AccessTokenUtil.getAccessToken();
        if(accessToken == null){
            return null;
        }
        jsapiTicket = getFromWechatServer(accessToken.getAccessToken());
        if(jsapiTicket != null){
            RedisUtil.set(JSAPI_TICKET_KEY,JSONObject.toJSONString(jsapiTicket),jsapiTicket.getExpiresIn()-60*10);
        }
        return jsapiTicket;
    }

    private static JsapiTicket getFromWechatServer(String accessToken){
        String url = String.format(GET_JAAPI_TICKET_URL,accessToken);
        JSONObject jsonObject = WechatUtils.get(url);
        if(jsonObject == null){
            logger.info("****获取token失败，微信返回结果为null");
            return null;
        }
        if(jsonObject.getIntValue("errcode") != 0){
            logger.info("****获取jsapiTicket失败 errcode:{}, errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
            return null;
        }
        JsapiTicket jsapiTicket = new JsapiTicket();
        jsapiTicket.setTicket(jsonObject.getString("ticket"));
        jsapiTicket.setExpiresIn(jsonObject.getInteger("expires_in"));
        return jsapiTicket;
    }
}
