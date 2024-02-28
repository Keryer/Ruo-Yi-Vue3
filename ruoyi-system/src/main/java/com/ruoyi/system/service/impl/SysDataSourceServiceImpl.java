package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.DataSource;
import com.ruoyi.system.mapper.SysDataSourceMapper;
import com.ruoyi.system.service.ISysDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDataSourceServiceImpl implements ISysDataSourceService {

    @Autowired
    private SysDataSourceMapper dataSourceMapper;

    @Override
    public int insertSource(DataSource source) {
        dataSourceMapper.insertSource(source);
        return 0;
    }

    @Override
    public List<DataSource> selectSource(DataSource source) {

        return dataSourceMapper.selectSource(source);
    }

    @Override
    public int deleteSource(Long[] sourceIds) {
        return dataSourceMapper.deleteSource(sourceIds);
    }
}
