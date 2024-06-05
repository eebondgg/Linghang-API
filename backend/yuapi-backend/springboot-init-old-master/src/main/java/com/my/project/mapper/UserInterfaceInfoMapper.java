package com.my.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.myapi.common.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author chen
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-05-07 19:52:55
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




