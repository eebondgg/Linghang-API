package com.my.myapiclientsdk.client;

import cn.hutool.http.HttpRequest;

/**
 * @author: crj
 * @Desc:
 * @create: 2024-05-16
 **/
public class PaperApiClient extends MyApiClient{

    public PaperApiClient(String accessKey, String secretKey) {
        super(accessKey, secretKey);
    }

    /**
     * 获取每日壁纸
     * @return
     */
    public String getDayWallpaperUrl(){
        return HttpRequest.post(GATEWAY_HOST+"/api/day/wallpaper")
                .addHeaders(getHeadMap("",accessKey,secretKey))
                .execute().body();
    }
}
