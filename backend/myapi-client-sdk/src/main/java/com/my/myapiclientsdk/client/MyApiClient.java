package com.my.myapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;

import java.util.HashMap;
import java.util.Map;

import static com.my.myapiclientsdk.utils.SignUtils.genSign;

/**
 * 调用第三方接口的客户端
 */
public class MyApiClient {
    protected static final String GATEWAY_HOST = "http://localhost:8090";

    protected String accessKey;
    protected String secretKey;

    public MyApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    protected static Map<String,String> getHeadMap(String body, String accessKey, String secretKey){
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put("accessKey",accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body",body);
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign",genSign(body,secretKey));
        return hashMap;
    }
}
