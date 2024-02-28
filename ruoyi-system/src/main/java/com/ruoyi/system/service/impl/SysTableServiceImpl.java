package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.domain.entity.DatahouseTable;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysRoleTableMapper;
import com.ruoyi.system.mapper.SysTableMapper;
import com.ruoyi.system.service.ISysTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;


import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据表 业务层处理
 *
 * @author Kery
 */
@Service
public class SysTableServiceImpl implements ISysTableService {

    @Autowired
    private SysTableMapper tableMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleTableMapper roleTableMapper;

    /**
     * 根据用户查询数据表列表
     *
     * @param userId 用户ID
     * @return 数据表列表
     */
    @Override
    public List<DatahouseTable> selectTableList(Long userId)
    {
        return selectTableList(new DatahouseTable(), userId);
    }

    @Override
    public List<DatahouseTable> selectTableList(DatahouseTable table, Long userId)
    {
        List<DatahouseTable> tableList = null;
        // 管理员显示所有数据表
        if (SysUser.isAdmin(userId))
        {
            tableList = tableMapper.selectTableList(table);
        }
        else
        {
            table.getParams().put("userId", userId);
            tableList = tableMapper.selectTableListByUserId(table);
        }
        return tableList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectTablePermsByUserId(Long userId)
    {
        List<String> perms = tableMapper.selectTablePermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectTablePermsByRoleId(Long roleId)
    {
        List<String> perms = tableMapper.selectTablePermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询数据表
     *
     * @param userId 用户ID
     * @return 数据表列表
     */
    @Override
    public List<DatahouseTable> selectTableTreeByUserId(Long userId)
    {
        List<DatahouseTable> tables = null;
        if (SecurityUtils.isAdmin(userId))
        {
            tables = tableMapper.selectTableTreeAll();
        }
        else
        {
            tables = tableMapper.selectTableTreeByUserId(userId);
        }
        return getChildPerms(tables, 0);
    }

    /**
     * 根据角色ID查询数据表
     *
     * @param roleId 角色ID
     * @return 选中数据表列表
     */
    @Override
    public List<Long> selectTableListByRoleId(Long roleId)
    {
        SysRole role = roleMapper.selectRoleById(roleId);
        return tableMapper.selectTableListByRoleId(roleId, role.isTableCheckStrictly());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param tables 数据表列表
     * @return 树结构列表
     */
    @Override
    public List<DatahouseTable> buildTableTree(List<DatahouseTable> tables)
    {
        List<DatahouseTable> returnList = new ArrayList<DatahouseTable>();
        List<Long> tempList = tables.stream().map(DatahouseTable::getTableId).collect(Collectors.toList());
        for (Iterator<DatahouseTable> iterator = tables.iterator(); iterator.hasNext();)
        {
            DatahouseTable table = (DatahouseTable) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(table.getMasterId()))
            {
                recursionFn(tables, table);
                returnList.add(table);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = tables;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param tables 数据表列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildTableTreeSelect(List<DatahouseTable> tables)
    {
        List<DatahouseTable> tableTrees = buildTableTree(tables);
        return tableTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据数据表ID查询信息
     *
     * @param tableId 数据表ID
     * @return 数据表信息
     */
    @Override
    public DatahouseTable selectTableById(Long tableId)
    {
        return tableMapper.selectTableById(tableId);
    }

    /**
     * 是否存在从表节点
     *
     * @param tableId 数据表ID
     * @return 结果
     */
    @Override
    public boolean hasChildByTableId(Long tableId)
    {
        int result = tableMapper.hasChildByTableId(tableId);
        return result > 0;
    }

    /**
     * 查询数据表是否存在用户
     *
     * @param tableId 数据表ID
     * @return 结果
     */
    @Override
    public boolean checkTableExistRole(Long tableId)
    {
        int result = roleTableMapper.checkTableExistRole(tableId);
        return result > 0;
    }

    /**
     * 新增保存数据表信息
     *
     * @param table 数据表信息
     * @return 结果
     */
    @Override
    public int insertTable(DatahouseTable table)
    {
        return tableMapper.insertTable(table);
    }

    /**
     * 修改保存数据表信息
     *
     * @param table 数据表信息
     * @return 结果
     */
    @Override
    public int updateTable(DatahouseTable table)
    {
        return tableMapper.updateTable(table);
    }

    /**
     * 删除数据表管理信息
     *
     * @param tableId 数据表ID
     * @return 结果
     */
    @Override
    public int deleteTableById(Long tableId)
    {
        return tableMapper.deleteTableById(tableId);
    }

    /**
     * 校验数据表名称是否唯一
     *
     * @param table 数据表信息
     * @return 结果
     */
    @Override
    public boolean checkTableNameUnique(DatahouseTable table)
    {
        Long tableId = StringUtils.isNull(table.getTableId()) ? -1L : table.getTableId();
        DatahouseTable info = tableMapper.checkTableNameUnique(table.getTableName(), table.getMasterId());
        if (StringUtils.isNotNull(info) && info.getTableId().longValue() != tableId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<DatahouseTable> getChildPerms(List<DatahouseTable> list, int parentId)
    {
        List<DatahouseTable> returnList = new ArrayList<DatahouseTable>();
        for (Iterator<DatahouseTable> iterator = list.iterator(); iterator.hasNext();)
        {
            DatahouseTable t = (DatahouseTable) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getMasterId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t 子节点
     */
    private void recursionFn(List<DatahouseTable> list, DatahouseTable t)
    {
        // 得到子节点列表
        List<DatahouseTable> childList = getChildList(list, t);
        t.setChildren(childList);
        for (DatahouseTable tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<DatahouseTable> it = childList.iterator();
                while (it.hasNext())
                {
                    DatahouseTable n = (DatahouseTable) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<DatahouseTable> getChildList(List<DatahouseTable> list, DatahouseTable t)
    {
        List<DatahouseTable> tlist = new ArrayList<DatahouseTable>();
        Iterator<DatahouseTable> it = list.iterator();
        while (it.hasNext())
        {
            DatahouseTable n = (DatahouseTable) it.next();
            if (StringUtils.isNotNull(n.getMasterId()) && n.getMasterId().longValue() == t.getTableId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<DatahouseTable> list, DatahouseTable t)
    {
        return getChildList(list, t).size() > 0;
    }
}
