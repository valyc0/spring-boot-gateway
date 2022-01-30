package com.javainuse.config.myconfig;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;



@Component
public class LoggingHeaderFilter extends AbstractGatewayFilterFactory<LoggingHeaderFilter.Config> {

	private static final Logger log  = LoggerFactory.getLogger(LoggingHeaderFilter.class);

	public LoggingHeaderFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		// Custom Pre Filter. Suppose we can extract JWT and perform Authentication
		return (exchange, chain) -> {
			log.debug("LoggingFilter" + exchange.getRequest());

			ServerHttpRequest req = exchange.getRequest();
			ServerWebExchangeUtils.addOriginalRequestUrl(exchange, req.getURI());
			
			ServerWebExchange  sw = exchange.getAttribute("org.springframework.web.server.adapter.DefaultServerWebExchange");
			
		    LinkedHashSet<URI> attr = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
            Optional<URI> option = attr.stream().findFirst();
            String origPath = option.get().toString();

			
			String path = req.getURI().getRawPath();
			String aa = req.getURI().getPath();
			log.debug(req.getURI().getPath());
			log.debug("REQUEST path:{}",origPath);
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