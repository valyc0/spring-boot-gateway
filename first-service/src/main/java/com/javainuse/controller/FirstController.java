package com.javainuse.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class FirstController {

	@GetMapping("/message")
	public String test(@RequestHeader Map<String, String> headers) {
	   
	    for (Map.Entry<String, String> entry : headers.entrySet()) {
	        System.out.println(entry.getKey() + "/" + entry.getValue());
	    }
		return "Hello JavaInUse Called in First Service";
	}
}
