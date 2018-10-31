package com.example.hackathon;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	@GetMapping("auth")
	public void testauth() {
		System.out.println("In Auth @@@@@@@@@@@@@@@@@@");
	}
	
}
