package com.javainuse.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.javainuse.service.MyService;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

	private static final Logger log  = LoggerFactory.getLogger(LoggingFilter.class);

	public LoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		// Custom Pre Filter. Suppose we can extract JWT and perform Authentication
		return (exchange, chain) -> {
			log.debug("LoggingFilter" + exchange.getRequest());

			ServerHttpRequest req = exchange.getRequest();
			ServerWebExchangeUtils.addOriginalRequestUrl(exchange, req.getURI());
			String path = req.getURI().getRawPath();
			log.debug("REQUEST path:{}",path);
			HttpHeaders headers = req.getHeaders();
		    for (Map.Entry<String, List<String>> header : headers.entrySet()) {
		    	
		    	log.debug("REQUEST header:key {} -value {}",header.getKey(),header.getValue());

		    }
			//exchange.getAttributes().put("URL_TO_SEND", host + path);
			return chain.filter(exchange);

		};
	}

	public static class Config {
		// Put the configuration properties
	}
}