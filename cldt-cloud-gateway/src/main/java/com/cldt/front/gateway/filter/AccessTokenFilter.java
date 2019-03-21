package com.cldt.front.gateway.filter;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.cldt.front.gateway.security.AccChannel;
import com.cldt.front.gateway.security.Subject;
import com.cldt.front.gateway.security.ZuulFilterOrder;
import com.cldt.utils.security.DESEncrypt;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Slf4j
@Component
public class AccessTokenFilter extends ZuulFilter {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();  
        HttpServletRequest request = ctx.getRequest();
        log.info("request path - {}", request.getRequestURI());
        String path = request.getRequestURI();
        if(StringUtils.isNoneBlank(path) && path.indexOf("/access_token") >= 0){
        	return true;
        }
		return false;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();  
        HttpServletRequest request = ctx.getRequest();
        ctx.setSendZuulResponse(false);
        String appId = request.getParameter("appId");
        AccChannel accChannel = AccChannel.getMerAccChannelByAppId(appId);
		if(accChannel == null) {
			ctx.setResponseStatusCode(403);// 返回错误码
			String message = "{\n" +
					"\"code\": 4031,\n" +
					"\"message\": \"参数非法\"\n" +
					"}";
			ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			ctx.setResponseBody(message);
			
		}else{
			ctx.setResponseStatusCode(200);
            String noncestr = RandomStringUtils.random(24, true, true);
            String accessToken = "gdb-acc-" + RandomStringUtils.random(6, true, true) + "-" + UUID.randomUUID();
            Subject subject = new Subject();
            subject.setAppId(appId);
            subject.setNoncestr(noncestr);
            subject.setAccChannel(accChannel);
            JSONObject json = new JSONObject();
            try {
				json.put("noncestr", noncestr);
				json.put("accessToken", accessToken);
			} catch (JSONException e) {
				log.error("封装返回json", e);
			}
            log.info("appId[{}] - accessToken result[{}]", appId, json);
            String encryptData = "";
			try {
				encryptData = Base64.encodeBase64String(json.toString().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				log.error("return result base64 encode", e);
			}
			encryptData = DESEncrypt.encode3Des(accChannel.getAppSecret(), encryptData);
            ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			ctx.setResponseBody("{\n" +
					"\"code\": 200,\n" +
					"\"message\": \"操作成功\",\n" +
					"\"result\":\"" + encryptData  + "\"\n" +
					"}");
			redisTemplate.opsForValue().set(accessToken, subject, 2, TimeUnit.HOURS);
		}
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return ZuulFilterOrder.ACCESS_TOKEN_FILTER_ORDER;
	}

}
