package com.my.myapiinterface.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.my.myapiinterface.entity.ImageResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author: crj
 * @Desc:
 * @create: 2024-05-16
 **/
@RestController
@RequestMapping("/day")
public class PaperController {

    @PostMapping("/wallpaper")
    public String getDayWallpaperUrl(){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("format","json");
        HttpResponse response = HttpRequest.post("https://tenapi.cn/v2/bing")
                .form(paramMap)
                .execute();
        String body = response.body();
        ImageResponse imageResponse = JSONUtil.toBean(body, ImageResponse.class);
        return imageResponse.getData().getUrl();
    }
}