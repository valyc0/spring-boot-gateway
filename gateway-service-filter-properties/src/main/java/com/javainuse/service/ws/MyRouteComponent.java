package com.javainuse.service.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.javainuse.APIGatewayApplication;
import com.javainuse.config.DinamicRouteFilter;
import com.javainuse.config.LoggingFilter;
import com.javainuse.config.MyTestFilter;

import reactor.core.publisher.Mono;

@Component
public class MyRouteComponent{
	
	private static final Logger logger  = LoggerFactory.getLogger(MyRouteComponent.class);
	

	@Autowired
	WsModifyResponse modifyResponse;
	
	@Autowired
	MyTestFilter myTestFilter;
	
	@Bean
    public RouteLocator myRouteSavingRequestBody(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("my-route-id",
                p -> p
                	.path("/websamples.countryinfo/**") //your own path filter
                	.filters(f -> f
        			    
                		.modifyRequestBody(String.class, String.class,
                              (webExchange, originalBody) -> {
                            	  webExchange.getAttributes().put("MYVAR", "myvar");
                                  if (originalBody != null) {
                                      logger.debug("Request body {}", originalBody);
                                      return Mono.just(originalBody);
                                  } else {
                                      return Mono.empty();
                                  }
                              })	
        			    
        			    
        			    .filter(myTestFilter.apply(new MyTestFilter.Config()))
        			    
                        .modifyResponseBody(String.class, String.class,
                            (webExchange, originalBody) -> {
                                if (originalBody != null) {
                                    logger.debug("Response body {}", originalBody);
                                    return Mono.just(modifyResponse.changeResponse(originalBody));
                                } else {
                                    return Mono.empty();
                                }
                            })
                       
                      

                    )
                    
                    .uri("http://webservices.oorsprong.org")
                    
                   
            )
            .build();
    }
	
	
	
}