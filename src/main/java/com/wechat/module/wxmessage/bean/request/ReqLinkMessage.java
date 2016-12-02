package com.wechat.module.wxmessage.bean.request;

/**
 * Created by CAI_GC on 2016/12/2.
 */
public class ReqLinkMessage  extends ReqBaseMessage{
	//接收到文本信息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
	
	
}
