package com.cldt.front.gateway.suport;

import java.io.Serializable;

/**
 * 客户端请求消息
 * 
 * @version 2017-10-21
 */
public class ClientMessage implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String msgId;
	
	/**
	 * 客户端类型
	 */
	private String cType;

	/**
	 * 参数内容
	 */
	private String data;

	/**
	 * 签名
	 */
	private String sign;

	public String getcType() {
		return cType;
	}

	public void setcType(String cType) {
		this.cType = cType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgId() {
		return msgId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
