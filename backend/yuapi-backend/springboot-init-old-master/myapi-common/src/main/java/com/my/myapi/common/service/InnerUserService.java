package com.my.myapi.common.service;


import com.my.myapi.common.model.entity.User;




/**
 * 用户服务
 *
 * @author yupi
 */
public interface InnerUserService {

    /**
     * 数据库中查询是否已经分配给用户密钥（accessKey,返回用户信息，为空表示不存在)
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
