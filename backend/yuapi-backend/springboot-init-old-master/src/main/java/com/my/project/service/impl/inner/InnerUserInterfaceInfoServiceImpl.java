package com.my.project.service.impl.inner;

import com.my.myapi.common.service.InnerUserInterfaceInfoService;
import com.my.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author: crj
 * @Desc:
 * @create: 2024-05-12
 **/

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId,userId);
    }
}
