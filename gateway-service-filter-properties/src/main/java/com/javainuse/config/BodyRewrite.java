package com.javainuse.config;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class BodyRewrite implements RewriteFunction<byte[], byte[]> {
	
	private static final Logger log  = LoggerFactory.getLogger(BodyRewrite.class);
	private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	
	
    @Override
    public Publisher<byte[]> apply(ServerWebExchange exchange, byte[] body) {
    	String originalBody=null;
    	originalBody =new String(body, StandardCharsets.UTF_8);
       
		
        String typeOfBody = exchange.getAttribute("TYPE_OF_BODY");
       
        log.debug("{}: originalBody:{}",typeOfBody,originalBody);
        exchange.getAttributes().put("REQUEST_BODY",originalBody);
        if (!ServerWebExchangeUtils.isAlreadyRouted(exchange)) {
            //return Mono.just(originalBody.getBytes());
        	return Mono.just(body);
        } else {
        // its the reponse body when already routed
        }
        
        return Mono.just(body);
    }
}