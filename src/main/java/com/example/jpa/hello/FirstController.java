package com.example.jpa.hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class FirstController {
	
	@RequestMapping(value = "/first-url", method = RequestMethod.GET)
	public void first() {
		
	
	}
	
	@ResponseBody   // 기본적인 뷰 페이지를 리턴하는 것이 아닌, 문자열을 리턴하기 위해 붙임
	@RequestMapping("/helloworld")
	public String helloworld() {
		
		return "hello world";
	}
}
