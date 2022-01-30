package com.javainuse.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
	public CustomFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		//Custom Pre Filter. Suppose we can extract JWT and perform Authentication
		return (exchange, chain) -> {
			System.out.println("First pre filter" + exchange.getRequest());
			
			
			String authHeader="aaaaaaaaaaaaaa";
			ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + authHeader)
					.header("aaa", "ciao") 
					.build();
			
			HttpHeaders headers = exchange.getRequest().getHeaders();
			
			
//			ServerHttpRequest mutatedRequest =  exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, "Bearer " + authHeader).build();
	        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
	        
	        
	        return chain.filter(mutatedExchange);
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