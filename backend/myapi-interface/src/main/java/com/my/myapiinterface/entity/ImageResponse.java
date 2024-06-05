package com.my.myapiinterface.entity;

import lombok.Data;

/**
 * @author: crj
 * @Desc:
 * @create: 2024-05-16
 **/
@Data
public class ImageResponse {
    private String code;
    private String msg;

    private Image data;
}
