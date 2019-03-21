package com.cldt.front.gateway.security;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;

public enum AccChannel {

	PROMO_H5("499718150bdf4cc8855ceaf067b9e3eb", "U6vHOpHXHbO9zOGOreKs0b6j", "平台优惠活动H5WEB", new String[]{"/v1/uac"}),
	MERCHANT_ANDROID_APP("3f1fbb3d34784e39b31f47c5f3a6b9a9", "3Y3FmFdIEeGBZzNb9YanG0ZV", "商户AndroidApp", new String[]{"/v1/uac"}),
	MERCHANT_IOS_APP("2db2b15f38e34f0da2d6cc0c0cb68456", "2GHJkdFDgODy4irlXgI7qJRi", "商户IOSApp", new String[]{"/v1/uac"}),
	;
	
	private String appId;
	private String appSecret;
	private String desc;
	private String[] basePaths;
	
	private AccChannel(String appId, String appSecret, String desc, String[] basePaths){
		this.appId = appId;
		this.appSecret = appSecret;
		this.desc = desc;
		this.basePaths = basePaths;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public String getDesc() {
		return desc;
	}

	public String[] getBasePaths() {
		return basePaths;
	}
	
	public static AccChannel getMerAccChannelByAppId(String appId){
		if(StringUtils.isBlank(appId)) {
			return null;
		}
		AccChannel[] list = AccChannel.values();
		for(AccChannel channel : list) {
			if(channel.appId.equals(appId)) {
				return channel;
			}
		}
		return null;
	}
	
	public static void main(String[] args){
		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
		String appSecret = RandomStringUtils.random(24, true, true);
		System.out.println(appSecret);
	}
}
