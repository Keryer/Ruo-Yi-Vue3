package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.DatahouseTable;

import java.util.List;
import java.util.Set;

/**
 * 数据表 业务层
 *
 * @author Kery
 */
public interface ISysTableService
{
    /**
     * 根据用户查询数据表列表
     *
     * @param userId 用户ID
     * @return 数据表列表
     */
    public List<DatahouseTable> selectTableList(Long userId);

    /**
     * 根据用户查询数据表列表
     *
     * @param table 数据表信息
     * @param userId 用户ID
     * @return 数据表列表
     */
    public List<DatahouseTable> selectTableList(DatahouseTable table, Long userId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectTablePermsByUserId(Long userId);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public Set<String> selectTablePermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询数据表树信息
     *
     * @param userId 用户ID
     * @return 数据表列表
     */
    public List<DatahouseTable> selectTableTreeByUserId(Long userId);

    /**
     * 根据角色ID查询数据表树信息
     *
     * @param roleId 角色ID
     * @return 数据表列表
     */
    public List<Long> selectTableListByRoleId(Long roleId);

    /**
     * 构建前端所需要的树结构
     *
     * @param tables 数据表列表
     * @return 树结构列表
     */
    public List<DatahouseTable> buildTableTree(List<DatahouseTable> tables);

    /**
     * 构建前端所需要的下拉树结构
     *
     * @param tables 数据表列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildTableTreeSelect(List<DatahouseTable> tables);

    /**
     * 根据数据表ID查询信息
     *
     * @param tableId 数据表ID
     * @return 数据表信息
     */
    public DatahouseTable selectTableById(Long tableId);

    /**
     * 是否存在从表节点
     *
     * @param tableId 数据表ID
     * @return 结果
     */
    public boolean hasChildByTableId(Long tableId);

    /**
     * 查询数据表是否存在角色
     *
     * @param tableId 数据表ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkTableExistRole(Long tableId);

    /**
     * 新增保存数据表信息
     *
     * @param table 数据表信息
     * @return 结果
     */
    public int insertTable(DatahouseTable table);

    /**
     * 修改保存数据表信息
     *
     * @param table 数据表信息
     * @return 结果
     */
    public int updateTable(DatahouseTable table);

    /**
     * 删除数据表管理信息
     *
     * @param tableId 数据表ID
     * @return 结果
     */
    public int deleteTableById(Long tableId);

    /**
     * 校验数据表名称是否唯一
     *
     * @param table 数据表信息
     * @return 结果
     */
    public boolean checkTableNameUnique(DatahouseTable table);

}
