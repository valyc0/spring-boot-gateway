package com.javainuse.service.ws;


import org.springframework.stereotype.Service;


@Service
public class WsModifyResponse {

   

    public String changeResponse(String s) {
    	
    	s = s.replace("Europe", "valyc-world");
    	return s;
		
	}

    

}