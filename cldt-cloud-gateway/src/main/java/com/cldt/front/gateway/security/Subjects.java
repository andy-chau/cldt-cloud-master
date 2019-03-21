package com.cldt.front.gateway.security;


/**
 * 会话对象封装
 * 
 * 注意不能在其它线程中获取Subject
 * 
 */
public abstract class Subjects {
	private static final ThreadLocal<String> tokenHolder = new ThreadLocal<String>();
	private static final ThreadLocal<String> subjectStrHolder = new ThreadLocal<String>();
	private static final ThreadLocal<Object> subjectHolder = new ThreadLocal<Object>();
	/**
	 * 设置token
	 * @param token
	 * @create_time 2016年7月21日 下午2:37:53
	 */
	public static void setToken(String token) {
		tokenHolder.set(token);
	}
	
	/**
	 * 设置会话主体
	 * @param subject
	 * @create_time 2016年7月21日 下午2:38:02
	 */
	public static void setSubject(Object subject) {
		subjectHolder.set(subject);
	}
	
	/**
	 * 获取token
	 * @return
	 * @create_time 2016年7月21日 下午2:38:20
	 */
	public static String getToken() {
		return tokenHolder.get();
	}
	
	/**
	 * 获取当前会话主体
	 * @return
	 * @create_time 2016年7月21日 下午2:38:43
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSubject() {
		return (T) subjectHolder.get();
	}
	
	/**
	 * 清除数据
	 * 
	 * @create_time 2016年7月21日 下午2:40:09
	 */
	public static void clear() {
		tokenHolder.remove();
		subjectStrHolder.remove();
		subjectHolder.remove();
	}
}
