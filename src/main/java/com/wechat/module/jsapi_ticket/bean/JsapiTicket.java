package com.wechat.module.jsapi_ticket.bean;

/**
 * api_ticket 是用于调用微信卡券JS API的临时票据，有效期为7200 秒，通过access_token 来获取
 * Created by CAI_GC on 2016/11/25.
 */
public class JsapiTicket {

    private String ticket;
    private int expiresIn;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
