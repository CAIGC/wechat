package com.wechat.module.utils;

import com.alibaba.fastjson.JSONObject;
import com.wechat.module.constant.WechatConfig;
import com.wechat.module.utils.bean.MyX509TrustManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by CAI_GC on 2016/11/28.
 */
public class WechatUtils {

    private static final Logger logger = LoggerFactory.getLogger(WechatUtils.class);

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return
     */
    public static String httpRequestReBackStr(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();
            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 对请求结果进行封装
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return Json 数据
     */
    private static JSONObject httpRequestCommon(String requestUrl, String requestMethod, String outputStr) {
        String str = httpRequestReBackStr(requestUrl, requestMethod, outputStr);
        if(str.startsWith("<xml>")){
            return xmlStrToJson(str);
        }
        return JSONObject.parseObject(str);
    }

    public static JSONObject get(String url) {
        return httpRequestCommon(url, "GET", null);
    }

    public static JSONObject post(String url, String postData) {
        return httpRequestCommon(url, "POST", postData);
    }

    /**
     * 页面签名
     * @param url
     * @param jsapi_ticket
     * @return
     */
    public static Map<String, String> signForWebPage(String url, String jsapi_ticket) {
        Map<String, String> rep = new HashMap<>();
        String nonce_str = createNonceStr();
        String timestamp = createTimestamp();
        String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
        String signature = DigestUtils.sha1Hex(str);
        rep.put("appId", WechatConfig.APPID);
        rep.put("url", url);
        rep.put("jsapi_ticket", jsapi_ticket);
        rep.put("nonceStr", nonce_str);
        rep.put("timestamp", timestamp);
        rep.put("signature", signature);
        return rep;
    }

    /**
     * 微信支付，签名，参考https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
     * 第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），
     * 使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。
     * 特别注意以下重要规则：
     * ◆ 参数名ASCII码从小到大排序（字典序）；
     * ◆ 如果参数的值为空不参与签名；
     * ◆ 参数名区分大小写；
     * ◆ 验证调用返回或微信主动通知签名时，传送的sign参数不参与签名，将生成的签名与该sign值作校验。
     * ◆ 微信接口可能增加字段，验证签名时必须支持增加的扩展字段
     * 第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，
     * 再将得到的字符串所有字符转换为大写，得到sign值signValue
     * @param parameters 集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序）
     * @return
     */
    public static String createSign(SortedMap<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> es = parameters.entrySet();
        Iterator<Map.Entry<String, String>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String k = entry.getKey();
            String v = entry.getValue();
            if (!k.equalsIgnoreCase("sign") && !v.equals("") && v != null) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + WechatConfig.PAY_SIGN_KEY);
        String result = DigestUtils.md5Hex(sb.toString());
        result = result.toUpperCase();
        return result;
    }

    public static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    public static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(random.nextInt(range)));
        }
        return sb.toString();
    }


    public static String mapToXmlStr(SortedMap<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set<Map.Entry<String, String>> es = parameters.entrySet();
        Iterator<Map.Entry<String, String>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String k = entry.getKey();
            String v = entry.getValue();
            if (!isNumeric(v)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static SortedMap<String, String> xmlStrToMap(String xmlStr){
        SortedMap<String, String> map = new TreeMap<String, String>();
        try {
            Document document = DocumentHelper.parseText(xmlStr);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();
            // 遍历所有子节点
            for (Element e : elementList){
                map.put(e.getName(), e.getText());
            }
        } catch (DocumentException e1) {
            logger.info("**************xmlStr:{}",xmlStr);
            e1.printStackTrace();
        }
        return map;
    }

    public static JSONObject xmlStrToJson(String xmlStr){
        JSONObject jsonObject = new JSONObject();
        try {
            Document document = DocumentHelper.parseText(xmlStr);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();
            // 遍历所有子节点
            for (Element e : elementList){
                jsonObject.put(e.getName(), e.getText());
            }
        } catch (DocumentException e1) {
            logger.info("**************xmlStr:{}",xmlStr);
            e1.printStackTrace();
        }
        return jsonObject;
    }

    private static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
