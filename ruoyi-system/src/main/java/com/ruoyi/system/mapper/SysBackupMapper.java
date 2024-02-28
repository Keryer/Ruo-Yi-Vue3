package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.DatahouseJob;

import java.util.List;

/**
 * 数据备份任务信息 数据层
 *
 * @author Kery
 */

public interface SysBackupMapper
{
    /**
     * 新增任务记录
     *
     * @param job 任务对象
     */
    public void insertJob(DatahouseJob job);

    /**
     * 查询任务记录
     *
     * @param job 任务对象
     * @return 任务对象集合
     */
    public List<DatahouseJob> selectJobList(DatahouseJob job);

    /**
     * 批量删除任务记录
     *
     * @param jobIds 需要删除的任务ID
     * @return 结果
     */
    public int deleteJobByIds(Long[] jobIds);

    /**
     * 清空任务记录
     *
     * @return 结果
     */
    public int cleanJob();


    /**
     * 更新任务记录
     *
     * @param job 任务对象
     * @return 影响行数
     */
    public int updateJob(DatahouseJob job);

    public DatahouseJob getJobById(Long jobId);
}
