package com.wechat.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by Administrator on 2015/8/26 0026.
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static Object getData(String url) throws Exception {
        JSONObject jo;
        String res = get(url);
        try {
            jo = JSONObject.parseObject(res);
        }catch (Exception e){
            logger.error("service execute error ，url:{},result:{}",url,res);
            e.printStackTrace();
            throw new Exception("service execute error");
        }
        if(jo != null && jo.get("status")!= null && (jo.get("status").equals("success")||jo.get("status").equals("ok")))
            return jo.get("data");
        return jo;

    }
    public static Object getData(String url,Object postParams) throws Exception {
        JSONObject jo;
        String res = post(url, postParams);
        try {
            jo = JSONObject.parseObject(res);
        }catch (Exception e){
            logger.error("service execute error ，url:{},result:{}",url,res);
            e.printStackTrace();
            throw new Exception("service execute error");
        }
        /*JSONObject jo = JSONObject.parseObject(post(url, postParams));*/
        if(jo != null && jo.get("status")!= null && (jo.get("status").equals("success")||jo.get("status").equals("ok")))
            return jo.get("data");
        else
            return jo;
    }

    public static String post(String url, Object object) {
        Map map = JSON.parseObject(JSON.toJSONString(object), Map.class);
        String content = null;
        try {
            content = Request.Post(url).body(new UrlEncodedFormEntity(createFormParams(map),"utf-8")).execute().returnContent().asString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return content;
    }

    private static List createFormParams(Map map) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Set es = map.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = String.valueOf(entry.getKey()) ;
            String v = String.valueOf(entry.getValue()) ;
            nameValuePairs.add(new BasicNameValuePair(k, v));
        }
        return nameValuePairs;
    }
    public static String get(String url) {
        String content = null;
        try {
            content = Request.Get(url).execute().returnContent().asString();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return content;
    }
    public static String get(String url,String parameter) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url + parameter;
            URL realUrl = new URL(urlName);
            URLConnection conn = realUrl.openConnection();
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String get(String url,Map<String,String> requestHeadMap) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            if(requestHeadMap != null){
                for(Map.Entry<String,String> entry:requestHeadMap.entrySet())
                {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    conn.setRequestProperty(key,value);
                }

            }
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
