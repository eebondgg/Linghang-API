package com.my.myapiinterface.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: crj
 * @Desc:
 * @create: 2024-05-16
 **/
@RestController
@RequestMapping("/random")
public class PoisonController {
    @GetMapping("/word")
    public String getRandomWork(){
        HttpResponse response = HttpRequest.get("https://tenapi.cn/v2/yiyan")
                .execute();
        return response.body();
    }
}
