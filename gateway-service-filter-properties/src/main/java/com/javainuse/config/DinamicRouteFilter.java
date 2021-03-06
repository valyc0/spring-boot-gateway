package com.javainuse.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.javainuse.APIGatewayApplication;
import com.javainuse.service.MyService;

@Component
public class DinamicRouteFilter extends AbstractGatewayFilterFactory<DinamicRouteFilter.Config> {
	
	private static final Logger logger  = LoggerFactory.getLogger(DinamicRouteFilter.class);

	@Autowired
	MyService myService;

	public DinamicRouteFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		// Custom Pre Filter. Suppose we can extract JWT and perform Authentication
		return (exchange, chain) -> {
			logger.debug("DinamicRouteFilter:" + exchange.getRequest());

			ServerHttpRequest req = exchange.getRequest();
			ServerWebExchangeUtils.addOriginalRequestUrl(exchange, req.getURI());
			String path = req.getURI().getRawPath();
			String host = myService.getNewHost(req);
			exchange.getAttributes().put("URL_TO_SEND", host + path);
			return chain.filter(exchange);

		};
	}

	public static class Config {
		// Put the configuration properties
	}
}