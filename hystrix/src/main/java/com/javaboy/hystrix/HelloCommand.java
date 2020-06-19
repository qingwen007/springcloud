package com.javaboy.hystrix;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;

public class HelloCommand extends HystrixCommand<String> {
    RestTemplate restTemplate;
    String name;

    public HelloCommand(Setter setter, RestTemplate restTemplate,String name) {
        super(setter);
        this.restTemplate=restTemplate;
        this.name=name;
    }

    @Override
    protected String run() throws Exception {

        return restTemplate.getForObject("http://provider/hello",String.class);
    }
    @Override
    protected String getFallback(){
        return "error-extends: "+getExecutionException().getMessage();
    }
    @Override
    protected String getCacheKey(){
        return  name;
    }
}
