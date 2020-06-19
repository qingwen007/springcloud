package com.javaboy.openfeign;

import com.javaboy.commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
public class HelloController {
    @Autowired
    HelloService helloService;
    @GetMapping("/hello")
    public String hello() throws UnsupportedEncodingException {
        String s = helloService.hello2("江南一点雨");
        System.out.println(s);
        User user = new User();
        user.setId(1);
        user.setUsername("java");
        user.setPassword("123");
        User user1 = helloService.addUser2(user);
        System.out.println(user1);
        helloService.deleteUser2(1);
        helloService.getUserByName(URLEncoder.encode("江南一点雨","utf-8"));
        return helloService.hello();
    }
    @GetMapping("/hello2")
    public String hello2(@RequestParam("name") String name){
        return helloService.hello();
    }

}
