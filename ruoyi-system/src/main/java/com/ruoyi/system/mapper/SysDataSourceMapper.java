package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.DataSource;

import java.util.List;

public interface SysDataSourceMapper {

    /**
     * 新增数据源信息
     *
     * @param source 数据源对象
     */
    public void insertSource(DataSource source);

    /**
     * 查询数据源信息
     *
     * @param source 数据源对象
     * @return 数据源对象列表
     */
    public List<DataSource> selectSource(DataSource source);

    /**
     * 批量删除数据源
     *
     * @param sourceIds 需要删除的数据源id列表
     * @return 结果
     */
    public int deleteSource(Long[] sourceIds);



}
