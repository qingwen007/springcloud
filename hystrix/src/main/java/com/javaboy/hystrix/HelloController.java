package com.javaboy.hystrix;


import com.javaboy.commons.User;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class HelloController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    HelloService helloService;
    @Autowired
    UserService userService;
    @GetMapping("/hello")
    public String hello(){
        return helloService.hello();
    }
    @GetMapping("/hello2")
    public String hello2() throws ExecutionException, InterruptedException {
        HystrixRequestContext.initializeContext();
        HelloCommand helloCommand=new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("javaboy")),restTemplate,"javaboy");
        String execute = helloCommand.execute();
        System.out.println(execute);
        HelloCommand helloCommand2=new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("javaboy")),restTemplate,"javaboy");
        Future<String> queue = helloCommand2.queue();
        String s = queue.get();
        System.out.println(s);

        return helloService.hello();
    }
    @GetMapping("/hello3")
    public void hello3() throws ExecutionException, InterruptedException {
        Future<String> stringFuture = helloService.hello2();
        String s = stringFuture.get();
        System.out.println(s);

    }
    @GetMapping("/hello4")
    public void hello4(){
        HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();
        String javaboy = helloService.hello3("javaboy");
        helloService.deleteUserByName("javaboy");
        javaboy=helloService.hello3("javaboy");
        hystrixRequestContext.close();
    }
    @GetMapping("/hello5")
    public void hello5() throws ExecutionException, InterruptedException {
        HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();
        Future<User> q1 = userService.getUserById(99);
        Future<User> q2 = userService.getUserById(98);
        Future<User> q3 = userService.getUserById(97);
        User u1 = q1.get();
        User u2 = q2.get();
        User u3 = q3.get();
        System.out.println(u1);
        System.out.println(u2);
        System.out.println(u3);
        Thread.sleep(2000);
        Future<User> q4 = userService.getUserById(96);
        User u4 = q4.get();
        System.out.println(u4);

        hystrixRequestContext.close();
    }
}
