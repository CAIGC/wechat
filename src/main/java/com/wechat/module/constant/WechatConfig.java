package com.wechat.module.constant;

/**
 * Created by CAI_GC on 2016/11/28.
 */
public class WechatConfig {

    public static String APPID;

    public static String APPSECRET;

    public static long DISCARDTIMEMIN;//默认110分钟失效,access_tokeny和ticket失效时间

    public static String MCH_ID;//微信支付分配的商户号

    public static String NOTIFY_URL;//支付成功回调的url

    public static String PAY_SIGN_KEY;//商户支付密钥Key

    public static String TRADE_TYPE;//支付类型

    public static String WX_HOST;

    public String getAPPID() {
        return APPID;
    }

    public void setAPPID(String APPID) {
        WechatConfig.APPID = APPID;
    }

    public String getAPPSECRET() {
        return APPSECRET;
    }

    public void setAPPSECRET(String APPSECRET) {
        WechatConfig.APPSECRET = APPSECRET;
    }

    public long getDISCARDTIMEMIN() {
        return DISCARDTIMEMIN;
    }

    public void setDISCARDTIMEMIN(long DISCARDTIMEMIN) {
        WechatConfig.DISCARDTIMEMIN = DISCARDTIMEMIN;
    }

    public String getMCH_ID() {
        return MCH_ID;
    }

    public void setMCH_ID(String MCH_ID) {
        WechatConfig.MCH_ID = MCH_ID;
    }

    public String getNOTIFY_URL() {
        return NOTIFY_URL;
    }

    public void setNOTIFY_URL(String NOTIFY_URL) {
        WechatConfig.NOTIFY_URL = NOTIFY_URL;
    }

    public static String getPAY_SIGN_KEY() {
        return PAY_SIGN_KEY;
    }

    public void setPAY_SIGN_KEY(String PAY_SIGN_KEY) {
        WechatConfig.PAY_SIGN_KEY = PAY_SIGN_KEY;
    }

    public String getTRADE_TYPE() {
        return TRADE_TYPE;
    }

    public void setTRADE_TYPE(String TRADE_TYPE) {
        WechatConfig.TRADE_TYPE = TRADE_TYPE;
    }

    public String getWX_HOST() {
        return WX_HOST;
    }

    public void setWX_HOST(String WX_HOST) {
        WechatConfig.WX_HOST = WX_HOST;
    }
}
