package com.wechat.module.access_token.bean;

/**
 * 微信全局access_token是公众号的全局唯一票据
 * access_token的存储至少要保留512个字符空间。access_token的有效期目前为2个小时，需定时刷新
 * Created by CAI_GC on 2016/11/25.
 */
public class AccessToken {

    private String accessToken;

    private int expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
