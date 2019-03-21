package com.cldt.front.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cldt.common.core.config.RedisConfiguration;


@Configuration
@Import(RedisConfiguration.class)
public class GatewayConfig {

}
