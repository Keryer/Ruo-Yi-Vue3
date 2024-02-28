package com.ruoyi.web.controller.datahouse;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.system.domain.DatahouseJob;
import com.ruoyi.system.service.ISysBackupService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;

import java.util.List;


/**
 * 备份任务记录
 *
 * @author Kery
 */
@RestController
@RequestMapping("/datahouse/backup")
public class BackupController extends BaseController
{
    @Autowired
    private ISysBackupService backupService;

    /**
     * 查询备份任务列表
     *
     * @param job 任务对象
     * @return 任务列表
     */
    @PreAuthorize("@ss.hasPermi('datahouse:backup:list')")
    @GetMapping("/list")
    public TableDataInfo list(DatahouseJob job)
    {
        startPage();
        List<DatahouseJob> list = backupService.selectJobList(job);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('datahouse:backup:add')")
    @Log(title = "备份任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody DatahouseJob job)
    {
        return toAjax(backupService.insertJob(job));
    }

    /**
     * 删除定时任务
     */
    @PreAuthorize("@ss.hasPermi('datahouse:backup:remove')")
    @Log(title = "备份任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}")
    public AjaxResult remove(@PathVariable Long[] jobIds) throws SchedulerException, TaskException
    {
        backupService.deleteJobByIds(jobIds);
        return success();
    }

    /**
     * 根据任务编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('datahouse:backup:remove')")
    @Log(title = "备份任务", businessType = BusinessType.DELETE)
    @GetMapping("/{jobId}")
    public AjaxResult edit(@PathVariable Long jobId)
    {
        return success(backupService.getJobById(jobId));
    }

    /**
     * 修改备份任务
     */
    @PreAuthorize("@ss.hasPermi('datahouse:backup:edit')")
    @Log(title = "备份任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody DatahouseJob job)
    {
        return toAjax(backupService.updateJob(job));
    }

}
