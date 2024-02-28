package com.ruoyi.system.domain;

/**
 * 角色和表关联 sys_role_table
 */
public class DatahouseRoleTable
{
    /** 角色ID */
    private Long roleId;

    /** 表ID */
    private Long tableId;

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getTableId()
    {
        return tableId;
    }

    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    @Override
    public String toString() {
        return "DatahouseRoleTable [roleId=" + roleId + ", tableId=" + tableId + "]";
    }
}
