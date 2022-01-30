package com.javainuse.config.myconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class MyConfigComponent{
	
	private static final Logger logger  = LoggerFactory.getLogger(MyConfigComponent.class);
	

	@Value("${my.path}")
	private String myPath;
	
	@Value("${my.uri}")
	private String myUri;
	
	@Autowired
	LoggingHeaderFilter loggingHeaderFilter;
	
	@Bean
    public RouteLocator myRouteSavingRequestBody(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("my-route-id",
                p -> p
                	//.path("/websamples.countryinfo/**") //your own path filter
                    .path(myPath) //your own path filter
                	.filters(f -> f
                			
                		.filter(loggingHeaderFilter.apply(new LoggingHeaderFilter.Config()))	
                		
                		.modifyRequestBody(String.class, String.class,
                              (webExchange, originalBody) -> {
                            	 
                                  if (originalBody != null) {
                                      logger.debug("Request body {}", originalBody);
                                      return Mono.just(originalBody);
                                  } else {
                                      return Mono.empty();
                                  }
                              })	
                		
                		
        			    
                        .modifyResponseBody(String.class, String.class,
                            (webExchange, originalBody) -> {
                                if (originalBody != null) {
                                    logger.debug("Response body {}", originalBody);
                                    return Mono.just(originalBody);
                                } else {
                                    return Mono.empty();
                                }
                            })
                    )
                    
                    .uri(myUri)
            )
            .build();
    }
	
	
	
}