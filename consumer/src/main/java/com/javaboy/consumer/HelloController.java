package com.javaboy.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@RestController
public class HelloController {
    @Autowired
    RestTemplate    restTemplate;
    @Autowired
    DiscoveryClient discoveryClient;
    @GetMapping("/hello2")
    public  String hello2(){
        List<ServiceInstance> provider = discoveryClient.getInstances("provider");
        ServiceInstance serviceInstance = provider.get(0);
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("http://");
        stringBuffer.append(host);
        stringBuffer.append(":");
        stringBuffer.append(port);
        stringBuffer.append("/hello");
        String s = restTemplate.getForObject(stringBuffer.toString(), String.class);
        return  s;

    }
    @GetMapping("/hello3")
    public  String hello3(){
        return  restTemplate.getForObject("http://provider/hello", String.class);
    }
    @GetMapping("/hello4")
    public  void hello4(){
        String s1 = restTemplate.getForObject("http://provider/hello2?name={1}", String.class, "javaboy");
        System.out.print(s1);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://provider/hello2?name={1}", String.class, "javaboy");
        String body = responseEntity.getBody();
        System.out.println("Body: "+body);
        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println("statusCode: "+statusCode);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        System.out.println("statusCode value: "+statusCodeValue);
        HttpHeaders headers = responseEntity.getHeaders();
        Set<String> keysest = headers.keySet();
        System.out.println("Header=================");
        for (String s : keysest) {
            System.out.println(headers.get(s));
        }
    }
}
