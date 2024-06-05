package com.my.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.myapi.common.model.entity.InterfaceInfo;

/**
* @author chen
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-05-04 22:41:39
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
