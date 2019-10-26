package com.abby.jiaqing.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping(value = "/test")
    public String test(){
        System.out.println("fsdfsdf");
        return "it works";
    }

    @RequestMapping(value = "/needLogin")
    public String login(){
        System.out.println("needlogin");
        return "needLogin";
    }
}
