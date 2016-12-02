package com.wechat.module.wxmessage.bean.response;


/**
 * Created by CAI_GC on 2016/12/2.
 */
public class RespTextMessage  extends RespBaseMessage{
	//回应的消息
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
	
	
}
