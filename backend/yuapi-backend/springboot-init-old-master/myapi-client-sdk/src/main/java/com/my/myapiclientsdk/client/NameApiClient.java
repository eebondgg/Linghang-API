package com.my.myapiclientsdk.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.my.myapiclientsdk.model.User;

import java.util.HashMap;

/**
 * @author: crj
 * @Desc:
 * @create: 2024-05-16
 **/
public class NameApiClient extends MyApiClient{
    public NameApiClient(String accessKey, String secretKey) {
        super(accessKey,secretKey);
    }
    public String getName(User user){
        String json = JSONUtil.toJsonStr(user);
        return HttpRequest.post(GATEWAY_HOST+"/api/name/user")
                .addHeaders(getHeadMap(json,accessKey,secretKey))
                .body(json)
                .execute().body();
    }
}
