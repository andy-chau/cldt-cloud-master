package com.cldt.front.gateway.filter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.cldt.utils.JacksonUtil;
import com.cldt.front.gateway.security.Constant;
import com.cldt.front.gateway.security.Subject;
import com.cldt.front.gateway.security.Subjects;
import com.cldt.front.gateway.security.ZuulFilterOrder;
import com.cldt.front.gateway.suport.ClientMessage;
import com.cldt.utils.security.MD5Encrypt;
import com.cldt.utils.security.SignUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import org.apache.commons.codec.binary.Base64;

@Slf4j
@Component
public class SignVerifyFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("RequestEntityDecodeFilter {} request to {}", request.getMethod(), request.getRequestURI());
		return isNeedValidate(request);
	}
	
	private boolean isNeedValidate(HttpServletRequest request) {
		String checkUrl = request.getRequestURI();
		String base = request.getContextPath();
		log.info("checkUrl : {}", checkUrl);
		for (String passUrl : Constant.signAuthUrls)
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
        InputStream in = (InputStream) ctx.get("requestEntity");
        String body = null;
		try {
			if (in == null) {
			    in = ctx.getRequest().getInputStream();
			}
			String bodyJson = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
			log.info("request body [{}] - length[{}]", bodyJson, bodyJson.length());
			ClientMessage clientMessage = JacksonUtil.parseJson(bodyJson, ClientMessage.class);
			String sign = clientMessage.getSign();
			String encryptStr = clientMessage.getData();
			if(StringUtils.isBlank(encryptStr) || !Base64.isBase64(encryptStr)) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(200);// 返回错误码
	            ctx.setResponseBody("{\n" +
						"\"code\": 4034,\n" +
						"\"message\": \"参数非法\"\n" +
						"}");// 返回错误内容
				return null;
			}
			body = new String(Base64.decodeBase64(encryptStr));
			String sortStr = SignUtil.sortJsonStringByAscii(body);
			Subject subject = Subjects.getSubject();
			if(subject == null) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(200);
				ctx.setResponseBody("{\n" +
	        			"\"code\": 4003,\n" +
	        			"\"message\": \"无效accessToekn\"\n" +
	        			"}");// 返回错误内容
	        	return null;
			}
			String validSignStr = subject.getNoncestr() + sortStr;
			String validSign = MD5Encrypt.md5(validSignStr);
			if(!sign.equalsIgnoreCase(validSign)) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(200);// 返回错误码
	            ctx.setResponseBody("{\n" +
						"\"code\": 4035,\n" +
						"\"message\": \"参数非法\"\n" +
						"}");// 返回错误内容
				return null;
			}
			log.info("request decode body：" + body);
		} catch (IOException e) {
			log.error("request decode", e);
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(403);// 返回错误码
            ctx.setResponseBody("{\n" +
					"\"code\": 4036,\n" +
					"\"message\": \"参数非法\"\n" +
					"}");// 返回错误内容
			return null;
		}
        final byte[] reqBodyBytes = body.getBytes();
        ctx.setRequest(new HttpServletRequestWrapper(request) {
            @Override
            public ServletInputStream getInputStream() throws IOException {
                return new ServletInputStreamWrapper(reqBodyBytes);
            }

            @Override
            public int getContentLength() {
                return reqBodyBytes.length;
            }

            @Override
            public long getContentLengthLong() {
                return reqBodyBytes.length;
            }
        });
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return ZuulFilterOrder.REQUEST_ENTITY_DECODE_FILTER_ORDER;
	}

}
