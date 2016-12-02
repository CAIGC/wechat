package com.wechat.module.wxmessage.bean.request;


/**
 * 消息基类（普通用户——公众账号）
 * Created by CAI_GC on 2016/12/2.
 */
 public class ReqBaseMessage {
	//开发者微信号
	private String ToUserName;
	//发送方账号（OpenID）
	private String FromUserName;
	//消息创建时间
	private long CreateTime;
	//消息类型（text/link...）
	private String MsgType;
	//消息id
	private long MsgId;
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public long getMsgId() {
		return MsgId;
	}
	public void setMsgId(long msgId) {
		MsgId = msgId;
	}
	
}
