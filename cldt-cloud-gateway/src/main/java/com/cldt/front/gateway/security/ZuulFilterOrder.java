package com.cldt.front.gateway.security;

public interface ZuulFilterOrder {

	int ACCESS_TOKEN_FILTER_ORDER = 0;
	
	int ACCESS_VERIFY_TOKEN_FILTER_ORDER = 10;
	
	int REQUEST_ENTITY_DECODE_FILTER_ORDER = 20;
	
	int AUTH_TOKEN_FILTER_ORDER = 30;
}
