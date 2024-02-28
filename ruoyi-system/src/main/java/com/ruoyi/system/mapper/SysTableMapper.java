package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.DatahouseTable;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *  数据表 数据层
 *
 * @author Kery
 */
public interface SysTableMapper
{
    /**
     * 查询数据表列表
     *
     * @param table 数据表信息
     * @return 数据表列表
     */
    public List<DatahouseTable> selectTableList(DatahouseTable table);

    /**
     * 根据用户所有权限
     *
     * @return 权限列表
     */
    public List<String> selectTablePerms();

    /**
     * 根据用户查询系统数据表列表
     *
     * @param table 数据表信息
     * @return 数据表列表
     */
    public List<DatahouseTable> selectTableListByUserId(DatahouseTable table);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public List<String> selectTablePermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> selectTablePermsByUserId(Long userId);

    /**
     * 根据用户ID查询数据表
     *
     * @return 数据表列表
     */
    public List<DatahouseTable> selectTableTreeAll();

    /**
     * 根据用户ID查询数据表
     *
     * @return 数据表列表
     */
    public List<DatahouseTable> selectTableTreeByUserId(Long userId);

    /**根据角色ID查询数据表树信息
     *
     * @param roleId 角色ID
     * @param tableCheckStrictly 数据表树选择项是否关联显示
     * @return 选中数据表列表
     */
    public List<Long> selectTableListByRoleId(@Param("roleId") Long roleId, @Param("tableCheckStrictly") boolean tableCheckStrictly);

    /**
     * 根据数据表ID查询信息
     *
     * @param tableId 数据表ID
     * @return 数据表信息
     */
    public DatahouseTable selectTableById(Long tableId);

    /**
     * 是否存在从表
     *
     * @param tableId 数据表ID
     * @return 结果
     */
    public int hasChildByTableId(Long tableId);

    /**
     * 新增数据表信息
     *
     * @param table 数据表信息
     * @return 结果
     */
    public int insertTable(DatahouseTable table);

    /**
     * 修改数据表信息
     *
     * @param table 数据表信息
     * @return 结果
     */
    public int updateTable(DatahouseTable table);

    /**
     * 删除数据表信息
     *
     * @param tableId 数据表ID
     * @return 结果
     */
    public int deleteTableById(Long tableId);

    /**
     * 检验数据表名称是否唯一
     *
     * @param tableName 数据表名称
     * @param masterId 主表ID
     * @return 结果
     */
    public DatahouseTable checkTableNameUnique(@Param("tableName") String tableName, @Param("masterId") Long masterId);


}
