package com.javainuse.service;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;


@Service
public class MyService {

   

    public String getNewHost(ServerHttpRequest req) {
    	String ret;
    	String someHeader = req.getHeaders().getFirst("myvar");
    	 String path = req.getURI().getRawPath();
    	 if("2".equals(someHeader)) {
    		 ret = "http://webservices.oorsprong.org";
    	 }
    	 else {
    		 ret = "http://webservices.oorsprong.org";
    	 }
    	
    	
		return ret;
       
    }

    

}