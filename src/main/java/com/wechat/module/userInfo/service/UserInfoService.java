package com.wechat.module.userInfo.service;

import com.alibaba.fastjson.JSONObject;
import com.wechat.module.access_token.utils.AccessTokenUtil;
import com.wechat.module.userInfo.bean.UserInfo;
import com.wechat.module.utils.WechatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by CAI_GC on 2016/11/29.
 */
@Service
public class UserInfoService {

    private final Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    /*关注后，获取用户基本信息接口*/
    private static final String GET_USER_INFO_BY_OPENID="https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";


    /**
     * 已关注用户，获取用户基本信息
     * @param openId
     * @return
     */
    public UserInfo getUserInfo(String openId){
        JSONObject jsonObject = WechatUtils.get(String.format(GET_USER_INFO_BY_OPENID, AccessTokenUtil.getAccessToken().getAccessToken(), openId));
        if(jsonObject.getString("errmsg") != null){
            logger.info("获取用户基本信息失败errcode:{}, errmsg:{}",jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));
            return null;
        }
        return JSONObject.parseObject(jsonObject.toString(),UserInfo.class);
    }
}
