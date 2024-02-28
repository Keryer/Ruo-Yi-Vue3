package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.DatahouseJob;
import com.ruoyi.system.mapper.SysBackupMapper;
import com.ruoyi.system.service.ISysBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据备份任务信息 服务层处理
 *
 * @author Kery
 */
@Service
public class SysBackupServiceImpl implements ISysBackupService
{

    @Autowired
    private SysBackupMapper backupMapper;

    /**
     * 新增任务记录
     *
     * @param job 任务对象
     * @return 形式返回值
     */
    @Override
    public int insertJob(DatahouseJob job)
    {
        backupMapper.insertJob(job);
        return 1;
    }

    /**
     * 查询任务记录集合
     *
     * @param job 任务对象
     * @return 任务记录集合
     */
    @Override
    public List<DatahouseJob> selectJobList(DatahouseJob job)
    {
        return backupMapper.selectJobList(job);
    }


    /**
     * 批量删除任务记录
     *
     * @param jobIds 需要删除的任务ID
     * @return 删除结果
     */
    @Override
    public int deleteJobByIds(Long[] jobIds)
    {
        return backupMapper.deleteJobByIds(jobIds);
    }


    /**
     * 清空任务记录
     */
    @Override
    public void cleanJob()
    {
        backupMapper.cleanJob();
    }

    /**
     * 更新任务记录
     */
    @Override
    public int updateJob(DatahouseJob job)
    {
        return backupMapper.updateJob(job);
    }

    @Override
    public DatahouseJob getJobById(Long jobId)
    {
        return backupMapper.getJobById(jobId);
    }


}
