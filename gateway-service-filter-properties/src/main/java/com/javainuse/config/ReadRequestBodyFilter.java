package com.javainuse.config;

import java.awt.image.DataBuffer;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class ReadRequestBodyFilter extends AbstractGatewayFilterFactory<ReadRequestBodyFilter.Config> {
	public ReadRequestBodyFilter() {
		super(Config.class);
	}
	private static final Logger log  = LoggerFactory.getLogger(ReadRequestBodyFilter.class);

	@Override
	public GatewayFilter apply(Config config) {
		//Custom Pre Filter. Suppose we can extract JWT and perform Authentication
		return (exchange, chain) -> {
			 
		        String body = exchange.getAttribute("REQUEST_BODY");
		        log.debug("chached body:{}",body);
		        // some processing

		        return chain.filter(exchange);
//			
//			
//			ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest()
//	                .mutate()
//	                .header("x-jwt-assertion", "aaa")
//	                .build();
			
			
			
			
//			//Custom Post Filter.Suppose we can call error response handler based on error code.
//			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//				System.out.println("First post filter");
//			}));
		};
	}

	public static class Config {
		// Put the configuration properties
	}
}