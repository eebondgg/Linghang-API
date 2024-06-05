package com.my.myapi.common.service;

import com.my.myapi.common.model.entity.InterfaceInfo;

/**
* @author chen
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-05-04 22:41:39
*/
public interface InnerInterfaceInfoService {

    /**
     * 数据库中查询模拟接口是否存在（请求路径，请求方法，返回接口信息，为空表示不存在）
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);
}
