package com.wechat.commons.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/11/25 0025.
 */
@Service
public class BaseServiceForApi {

    protected String makeUrl(String host,String url,Object... values){

        StringBuffer buffer = new StringBuffer();
        buffer = StringUtils.isBlank(host)?buffer.append(url):buffer.append(host).append(url);
        if(values == null){
            return buffer.toString();
        }
        for(Object obj:values){
            if(obj==null){
                continue;
            }
            buffer.append(obj);
        }
        return buffer.toString();
    }
}
