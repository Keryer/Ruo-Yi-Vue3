package com.ruoyi.system.service;

import com.ruoyi.system.domain.DataSource;

import java.util.List;

/**
 * 数据源信息 服务层
 *
 * @author Kery
 */
public interface ISysDataSourceService
{
    /**
     * 新增数据源对象
     *
     * @param source 数据源对象
     * @return 形式结果
     */
    public int insertSource(DataSource source);

    /**
     * 查询数据源对象
     *
     * @param source 数据源对象
     * @return 数据源集合
     */
    public List<DataSource> selectSource(DataSource source);

    /**
     * 批量删除数据源
     *
     * @param sourceIds 需要删除的数据源ID列表
     * @return 结果
     */
    public int deleteSource(Long[] sourceIds);


}
