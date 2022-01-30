package com.javainuse.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class MyTestFilter extends AbstractGatewayFilterFactory<MyTestFilter.Config> {

	private static final Logger log = LoggerFactory.getLogger(MyTestFilter.class);

	public MyTestFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		// Custom Pre Filter. Suppose we can extract JWT and perform Authentication
		return (exchange, chain) -> {
			log.debug("MyTestFilter" + exchange.getRequest());

			String s = exchange.getAttribute("MYVAR");
			log.debug("variable:"+s);

			return chain.filter(exchange);

		};
	}

	public static class Config {
		// Put the configuration properties
	}
}