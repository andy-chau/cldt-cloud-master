package com.cldt.front.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cldt.front.gateway.security.Constant;
import com.cldt.front.gateway.security.Subject;
import com.cldt.front.gateway.security.Subjects;
import com.cldt.front.gateway.security.ZuulFilterOrder;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Slf4j
@Component
public class AccessVerifyTokenFilter extends ZuulFilter {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("AccessVerifyTokenFilter {} request to {}", request.getMethod(), request.getRequestURI());
        
		return isNeedValidate(request);
	}
	
	private boolean isNeedValidate(HttpServletRequest request) {
		String checkUrl = request.getRequestURI();
		String base = request.getContextPath();
		log.info("checkUrl : {}", checkUrl);
		for (String passUrl : Constant.accAuthUrls)
		{
			String url = base + passUrl;
			if (checkUrl.endsWith(url)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpServletRequest request = ctx.getRequest();
        String accessToken = request.getHeader("accessToken");
        if(StringUtils.isBlank(accessToken)) {
        	accessToken = request.getParameter("accessToken");
        }
        log.info("accessToken[{}] - reqUrl[{}]", accessToken, request.getRequestURI());
        if(StringUtils.isBlank(accessToken)) {
        	ctx.setSendZuulResponse(false);
        	ctx.setResponseStatusCode(403);// 返回错误码
        	ctx.setResponseBody("{\n" +
        			"\"code\": 4032,\n" +
        			"\"message\": \"非法请求\"\n" +
        			"}");// 返回错误内容
        	return null;
        }
        Subject subject = (Subject)redisTemplate.opsForValue().get(accessToken);
        log.info("accessToken[{}] - redis subject[{}]", accessToken, JSON.toJSONString(subject));
        if(subject == null) {
        	ctx.setSendZuulResponse(false);
        	ctx.setResponseStatusCode(200);// 返回错误码
        	ctx.setResponseBody("{\n" +
        			"\"code\": 4003,\n" +
        			"\"message\": \"无效accessToekn\"\n" +
        			"}");// 返回错误内容
        	return null;
        }
        if(!isAuthModel(request.getRequestURI(), subject)){
        	ctx.setSendZuulResponse(false);
        	ctx.setResponseStatusCode(403);// 返回错误码
        	ctx.setResponseBody("{\n" +
        			"\"code\": 4033,\n" +
        			"\"message\": \"appId没有权限访问该资源\"\n" +
        			"}");// 返回错误内容
        	return null;
        }
        Subjects.setSubject(subject);
		return null;
	}

	private boolean isAuthModel(String checkUrl, Subject subject) {
        for (String passUrl : subject.getAccChannel().getBasePaths())
		{
			if (checkUrl.startsWith(passUrl)) {
				return true;
			}
		}
        return false;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return ZuulFilterOrder.ACCESS_VERIFY_TOKEN_FILTER_ORDER;
	}

}
