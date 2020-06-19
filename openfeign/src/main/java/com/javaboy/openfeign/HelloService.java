package com.javaboy.openfeign;

import com.javaboy.api.IUserService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("provider")
public interface HelloService extends IUserService {


}
