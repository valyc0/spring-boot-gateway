package com.javainuse.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.javainuse.service.MyService;

import reactor.core.publisher.Flux;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

	private static final Logger log  = LoggerFactory.getLogger(AuthorizationFilter.class);

	public AuthorizationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		// Custom Pre Filter. Suppose we can extract JWT and perform Authentication
		return (exchange, chain) -> {
		
			if (isAuthorizationTokenValid(exchange.getRequest().getHeaders().get("Authorization"))) 
			       return chain.filter(exchange);
			    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			    //Add some custom data in body of the response,
			    //Returning "Unauthorized" in the body here
			    return exchange.getResponse().writeWith(Flux.just(new DefaultDataBufferFactory().wrap("Unauthorized".getBytes())));

		};
	}

	public static class Config {
		// Put the configuration properties
	}
	
	 private boolean isAuthorizationTokenValid(List<String> authorizationTokens){
		    //Your logic here
		    return false;
		  }
}