package com.example.jpa.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // controller 에 @Requestbody를 달지 않아도 스트링으로 리턴 가능! (controller 처럼 기본적인 뷰 타입 리턴이 아님)
public class SecondController {
	
	@GetMapping("/api/helloworld")   // 최종적인 형태
	public String helloRestApi() {
		
		return "hello rest api";
	}
}
