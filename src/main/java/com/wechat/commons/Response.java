package com.wechat.commons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by CAI_GC on 2016/11/28.
 */
public class Response implements Serializable{

    private static final String RESPONSE_CODE = "status";
    private static final String RESPONSE_DATA = "data";
    private static final String RESPONSE_MESSAGE = "message";

    public static Map<String,Object> success(final Object data){
        return new HashMap<String,Object>(){
            private static final long serialVersionUID = 8794654518385734933L;
            {
                put(RESPONSE_CODE, "ok");
                put(RESPONSE_DATA, data);
            }
        };
    }
    public static Map<String,Object> success(final Object data,final Object message){
        return new HashMap<String,Object>(){
            private static final long serialVersionUID = 8794654518385734933L;
            {
                put(RESPONSE_CODE, "ok");
                put(RESPONSE_DATA, data);
                put(RESPONSE_MESSAGE,message);
            }
        };
    }
    public static Map<String,Object> success(){
        return success(null);
    }
    public static Map<String,Object> error(final Object data){
        return new HashMap<String,Object>(){
            private static final long serialVersionUID = 8794654518385734933L;
            {
                put(RESPONSE_CODE, "false");
                put(RESPONSE_DATA, data);
            }
        };
    }

    public static Map<String,Object> error(final Object data,final Object message){
        return new HashMap<String,Object>(){
            private static final long serialVersionUID = 8794654518385734933L;
            {
                put(RESPONSE_CODE, "false");
                put(RESPONSE_DATA, data);
                put(RESPONSE_MESSAGE,message);
            }
        };
    }
    public static Map<String,Object> error(){
        return error(null);
    }
}
