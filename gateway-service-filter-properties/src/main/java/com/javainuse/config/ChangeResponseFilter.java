package com.javainuse.config;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;

import com.javainuse.service.MyService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ChangeResponseFilter extends AbstractGatewayFilterFactory<ChangeResponseFilter.Config> {

	private static final Logger log  = LoggerFactory.getLogger(ChangeResponseFilter.class);

	public ChangeResponseFilter() {
		super(Config.class);
	}
	
	
	@Override
	public GatewayFilter apply(Config config) {
		// Custom Pre Filter. Suppose we can extract JWT and perform Authentication
		return (exchange, chain) -> {

			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				System.out.println("First post filter");
				
				ServerHttpResponse originalResponse = exchange.getResponse();
				DataBufferFactory bufferFactory = originalResponse.bufferFactory();
				ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
		            @Override
		            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
		            	if (body instanceof Flux) {
		                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
		                    return super.writeWith(fluxBody.map(dataBuffer -> {
		                    	// probably should reuse buffers 
		                    	byte[] content = new byte[dataBuffer.readableByteCount()];
		                    	dataBuffer.read(content);
		                    	byte[] uppedContent = new String(content, Charset.forName("UTF-8")).toUpperCase().getBytes();
		                    	return bufferFactory.wrap(uppedContent);
		                    }));
		            	}
		            	return super.writeWith(body); // if body is not a flux. never got there.
		            }			
				};
				exchange.mutate().response(decoratedResponse).build();
				//chain.filter(exchange.mutate().response(decoratedResponse).build()); // replace response with decorator
				
				
			}));
		};
	}

	
	public GatewayFilter applyold(Config config) {
		// Custom Pre Filter. Suppose we can extract JWT and perform Authentication
		return (exchange, chain) -> {
			ServerHttpResponse originalResponse = exchange.getResponse();
			DataBufferFactory bufferFactory = originalResponse.bufferFactory();
			ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
	            @Override
	            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
	            	if (body instanceof Flux) {
	                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
	                    return super.writeWith(fluxBody.map(dataBuffer -> {
	                    	// probably should reuse buffers 
	                    	byte[] content = new byte[dataBuffer.readableByteCount()];
	                    	dataBuffer.read(content);
	                    	byte[] uppedContent = new String(content, Charset.forName("UTF-8")).toUpperCase().getBytes();
	                    	return bufferFactory.wrap(uppedContent);
	                    }));
	            	}
	            	return super.writeWith(body); // if body is not a flux. never got there.
	            }			
			};
			return chain.filter(exchange.mutate().response(decoratedResponse).build()); // replace response with decorator
		};
	}
	

	public static class Config {
		// Put the configuration properties
	}
	
	 
}