package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目实体表 datahouse_table
 *
 * @author Kery
 */
public class DatahouseTable extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 表ID */
    private Long tableId;

    /** 表名称 */
    private String tableName;

    /** 表中文名称 */
    private String tableNameZh;

    /** 主表ID */
    private Long masterId;

    /** 主表名称 */
    private String masterName;

    /** 所属项目 */
    private String project;

    /** 子表 */
    private List<DatahouseTable> children = new ArrayList<DatahouseTable>();

    /** 权限 */
    private String perms;


    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableNameZh() {
        return tableNameZh;
    }

    public void setTableNameZh(String tableNameZh) {
        this.tableNameZh = tableNameZh;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public List<DatahouseTable> getChildren() {
        return children;
    }

    public void setChildren(List<DatahouseTable> children) {
        this.children = children;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    @Override
    public String toString() {
        return "DatahouseTable{" +
                "tableId=" + tableId +
                ", tableName='" + tableName + '\'' +
                ", tableNameZh='" + tableNameZh + '\'' +
                ", masterId='" + masterId + '\'' +
                ", masterName='" + masterName + '\'' +
                ", project='" + project +
                '}';
    }
}
