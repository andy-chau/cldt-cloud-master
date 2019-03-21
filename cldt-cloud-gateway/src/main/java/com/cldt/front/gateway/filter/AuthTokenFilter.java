package com.cldt.front.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.cldt.front.gateway.security.Constant;
import com.cldt.front.gateway.security.ZuulFilterOrder;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Slf4j
@Component
public class AuthTokenFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();  
        HttpServletRequest request = ctx.getRequest();
		log.info("request path - {}", request.getRequestURI());
		return isNeedValidate(request);
	}
	
	private boolean isNeedValidate(HttpServletRequest request) {
		String checkUrl = request.getRequestURI();
		String base = request.getContextPath();
		log.info("checkUrl : {}", checkUrl);
		for (String passUrl : Constant.userAuthUrls)
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
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return ZuulFilterOrder.AUTH_TOKEN_FILTER_ORDER;
	}

}
