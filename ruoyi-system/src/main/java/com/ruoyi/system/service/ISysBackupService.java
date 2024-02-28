package com.ruoyi.system.service;

import com.ruoyi.system.domain.DatahouseJob;

import java.util.List;

/**
 * 数据备份任务信息 服务层
 *
 * @author Kery
 */
public interface ISysBackupService
{
    /**
     * 新增任务对象
     *
     * @param job 任务对象
     * @return 形式结果
     */
    public int insertJob(DatahouseJob job);

    /**
     * 查询任务集合
     *
     * @param job 任务对象
     * @return 任务集合
     */
    public List<DatahouseJob> selectJobList(DatahouseJob job);

    /**
     * 批量删除任务
     *
     * @param jobIds 需要删除的任务ID
     * @return 结果
     */
    public int deleteJobByIds(Long[] jobIds);


    /**
     * 清空任务
     */
    public void cleanJob();


    public int updateJob(DatahouseJob job);

    public DatahouseJob getJobById(Long jobId);
}
