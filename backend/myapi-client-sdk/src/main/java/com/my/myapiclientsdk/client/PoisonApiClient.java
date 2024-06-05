package com.my.myapiclientsdk.client;

import cn.hutool.http.HttpRequest;

/**
 * @author: crj
 * @Desc:
 * @create: 2024-05-16
 **/
public class PoisonApiClient extends MyApiClient{
    public PoisonApiClient(String accessKey, String secretKey) {
        super(accessKey, secretKey);
    }

    public String getRandomWork(){
        return HttpRequest.get(GATEWAY_HOST+"/api/random/word")
                .addHeaders(getHeadMap("",accessKey,secretKey))
                .execute().body();
    }
}
