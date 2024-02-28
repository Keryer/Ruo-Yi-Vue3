package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;

public class DatahouseProject extends BaseEntity {
    /**
     * 项目ID
     */
    @TableId(value = "project_id", type = IdType.AUTO)
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName = "";

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
