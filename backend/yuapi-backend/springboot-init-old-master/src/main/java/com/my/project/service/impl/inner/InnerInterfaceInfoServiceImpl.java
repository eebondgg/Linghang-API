package com.my.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.myapi.common.model.entity.InterfaceInfo;
import com.my.myapi.common.service.InnerInterfaceInfoService;
import com.my.project.common.ErrorCode;
import com.my.project.exception.BusinessException;
import com.my.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author: crj
 * @Desc:
 * @create: 2024-05-12
 **/
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;
    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url,method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",url);
        queryWrapper.eq("method",method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
