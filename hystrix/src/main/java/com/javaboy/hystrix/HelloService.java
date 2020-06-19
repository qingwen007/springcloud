package com.javaboy.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;
    @HystrixCommand(fallbackMethod = "error",ignoreExceptions = ArithmeticException.class)
    public String hello(){

        return restTemplate.getForObject("http://provider/hello",String.class);
    }
    @HystrixCommand(fallbackMethod = "error")
    public Future<String> hello2(){
        return new AsyncResult<String>(){
            @Override
            public String invoke() {
                return restTemplate.getForObject("http://provider/hello",String.class);
            }
        };
    }
    @HystrixCommand(fallbackMethod = "error2")
    @CacheResult
    public String hello3(String name){

        return restTemplate.getForObject("http://provider/hello2?name={1}",String.class,name);
    }
    @HystrixCommand
    @CacheRemove(commandKey = "hello3")
    public String deleteUserByName(String name){
        return  null;
    }
    public String error(Throwable t){

        return "error: "+t.getMessage();
    }
    public String error2(String name){

        return "error2: "+name;
    }
}
