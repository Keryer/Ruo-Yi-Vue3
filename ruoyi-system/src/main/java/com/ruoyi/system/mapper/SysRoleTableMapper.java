package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.DatahouseRoleTable;

import java.util.List;

/**
 * 角色与数据表关联表 数据层
 *
 * @author Kery
 */
public interface SysRoleTableMapper
{
    /**
     * 查询数据表使用数量
     *
     * @param tableId 数据表ID
     * @return 结果
     */
    public int checkTableExistRole(Long tableId);

    /**
     * 通过角色ID删除角色和数据表关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleTableByRoleId(Long roleId);

    /**
     * 批量删除角色数据表关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleTable(Long[] ids);

    /**
     * 批量新增角色数据表信息
     *
     * @param roleTableList 角色数据表列表
     * @return 结果
     */
    public int batchRoleTable(List<DatahouseRoleTable> roleTableList);
}
