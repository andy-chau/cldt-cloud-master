package com.cldt.front.gateway.security;

import java.io.Serializable;

/**
 * 会话主体
 */
public class Subject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Id */
	protected String appId;
	
	protected String noncestr;
	
	protected AccChannel accChannel;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public AccChannel getAccChannel() {
		return accChannel;
	}

	public void setAccChannel(AccChannel accChannel) {
		this.accChannel = accChannel;
	}

}
