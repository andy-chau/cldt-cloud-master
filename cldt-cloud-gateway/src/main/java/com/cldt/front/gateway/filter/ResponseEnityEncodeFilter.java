package com.cldt.front.gateway.filter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Slf4j
@Component
public class ResponseEnityEncodeFilter extends ZuulFilter {
	 
	@Override
	public boolean shouldFilter() {
		return false;
	}

	@Override
	public Object run() {
		try {
            RequestContext context = RequestContext.getCurrentContext();
            InputStream stream = context.getResponseDataStream();
            String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            log.info("reponse body orig [{}]",body);
            if (StringUtils.isNotBlank(body)) {
            	body = new String(Base64.encodeBase64String(body.getBytes("UTF-8")));
            }
            log.info("reponse body encode [{}]",body);
            context.setResponseBody(body);
        } catch (IOException e) {
            log.error("reponse encode", e);
        }
        return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.POST_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 2;
	}

}
