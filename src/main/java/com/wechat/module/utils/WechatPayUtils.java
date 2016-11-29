package com.wechat.module.utils;

import com.alibaba.fastjson.JSONObject;
import com.wechat.module.constant.WechatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付相关工具类
 * Created by CAI_GC on 2016/11/29.
 */
public class WechatPayUtils {

    private static final Logger logger = LoggerFactory.getLogger("wechatPay");

    /*统一下单接口*/
    public static final String GET_UNIFIED_ORDER_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";



    public static SortedMap<String, String> wxPay(SortedMap<String, String> parameters) {
        SortedMap<String, String> rep = new TreeMap<String, String>();
        rep.put("appId", WechatConfig.APPID);
        rep.put("timeStamp", WechatUtils.createTimestamp());
        rep.put("nonceStr", WechatUtils.getRandomString(32));
        rep.put("package", "prepay_id=" + getPrepayId(getUnifiedOrder(parameters)));
        rep.put("signType", "MD5");
        rep.put("paySign", WechatUtils.createSign(rep));
        return rep;
    }

    /**
     * 统一下单接口
     *
     * @param parameters
     * @return
     */
    public static JSONObject getUnifiedOrder(SortedMap<String, String> parameters) {
        String signValue = WechatUtils.createSign(parameters);
        parameters.put("sign", signValue);
        String xmlData = WechatUtils.mapToXmlStr(parameters);
        JSONObject jsonObject = WechatUtils.post(GET_UNIFIED_ORDER_URL, xmlData);
        return jsonObject;
    }

    public static String getPrepayId(JSONObject jsonObject) {
        String prepayId = null;
        if (jsonObject.getString("return_code").equalsIgnoreCase("SUCCESS")) {
            if (jsonObject.getString("result_code").equalsIgnoreCase("SUCCESS")) {
                prepayId = jsonObject.getString("prepay_id");
            } else {
                logger.info("*****错误代码err_code:{}", jsonObject.getString("err_code"));
                logger.info("*****e错误代码描述rr_code_des:{}", jsonObject.getString("err_code_des"));
            }
        } else {
            logger.info("*****返回信息return_msg:{}", jsonObject.getString("return_msg"));
        }
        return prepayId;
    }


    public static SortedMap<String, String> createPackage(String orderCode, String body, String totalFee, String remoteAddrIP, String openId, String notifyUrl) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("appid", WechatConfig.APPID);
        parameters.put("attach", orderCode);
        parameters.put("mch_id", WechatConfig.MCH_ID);
        parameters.put("nonce_str", WechatUtils.getRandomString(32));
        parameters.put("body", body);
        parameters.put("out_trade_no", orderCode + "wenjin");
        parameters.put("total_fee", totalFee);
        parameters.put("spbill_create_ip", remoteAddrIP);// "58.60.170.191"
        parameters.put("notify_url", notifyUrl);
        parameters.put("trade_type", WechatConfig.TRADE_TYPE);
        parameters.put("openid", openId);
        return parameters;
    }


}
