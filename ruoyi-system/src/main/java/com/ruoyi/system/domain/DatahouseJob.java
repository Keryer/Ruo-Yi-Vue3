package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *  备份任务信息
 *
 * @author Kery
 */
public class DatahouseJob extends BaseEntity
{
    /** 任务ID */
    @TableId(value = "job_id", type = IdType.AUTO)
    private Long jobId;

    /** 项目名称 */
    private String projectName = "";

    /** 冷藏地址 */
    private String storageAddress = "";

    /** 中转地址 */
    private String transferStation = "";

    /** 备注 */
    private String comment = "";

    /** 状态 */
    private String state = "";

    /** 处理时间 */
    private String disposeTime = "";

    /** 操作 */
    private String operation = "";

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStorageAddress() {
        return storageAddress;
    }

    public void setStorageAddress(String storageAddress) {
        this.storageAddress = storageAddress;
    }

    public String getTransferStation() {
        return transferStation;
    }

    public void setTransferStation(String transferStation) {
        this.transferStation = transferStation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeTime(String disposeTime) {
        this.disposeTime = disposeTime;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("jobId", getJobId())
                .append("projectName", getProjectName())
                .append("storageAddress", getStorageAddress())
                .append("transferStation", getTransferStation())
                .append("comment", getComment())
                .append("state", getState())
                .append("disposeTime", getDisposeTime())
                .append("operation", getOperation())
                .append("createTime", getCreateTime())
                .toString();
    }
}
