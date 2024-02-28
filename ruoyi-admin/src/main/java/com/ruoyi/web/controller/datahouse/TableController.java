package com.ruoyi.web.controller.datahouse;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.domain.entity.DatahouseTable;
import com.ruoyi.system.service.ISysTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ruoyi.common.core.domain.AjaxResult.*;
import static com.ruoyi.common.utils.SecurityUtils.getUserId;
import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 数据表信息
 *
 * @author Kery
 */
@RestController
@RequestMapping("/datahouse/table")
public class TableController extends BaseController {
    @Autowired
    private ISysTableService tableService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@ss.hasPermi('datahouse:table:list')")
    @GetMapping("/list")
    public AjaxResult list(DatahouseTable table)
    {
        List<DatahouseTable> list = tableService.selectTableList(table, getUserId());
        return success(list);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('datahouse:table:query')")
    @GetMapping(value = "/{tableId}")
    public AjaxResult getInfo(@PathVariable Long tableId)
    {
        return success(tableService.selectTableById(tableId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeSelect(DatahouseTable table)
    {
        List<DatahouseTable> tables = tableService.selectTableList(table, getUserId());
        return success(tableService.buildTableTreeSelect(tables));
    }


    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleTableTreeselect/{roleId}")
    public AjaxResult roleTableTreeSelect(@PathVariable("roleId") Long roleId)
    {
        List<DatahouseTable> tables = tableService.selectTableList(getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", tableService.selectTableListByRoleId(roleId));
        ajax.put("tables", tableService.buildTableTreeSelect(tables));
        return ajax;
    }

    /**
     * 新增菜单
     */
    @PreAuthorize("@ss.hasPermi('datahouse:table:add')")
    @Log(title = "数据表管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody DatahouseTable table)
    {
        if (!tableService.checkTableNameUnique(table))
        {
            return error("新增数据表'" + table.getTableName() + "'失败，数据表名称已存在");
        }
        table.setCreateBy(getUsername());
        return toAjax(tableService.insertTable(table));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@ss.hasPermi('datahouse:table:edit')")
    @Log(title = "数据表管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody DatahouseTable table)
    {
        if (!tableService.checkTableNameUnique(table))
        {
            return error("修改数据表'" + table.getTableName() + "'失败，数据表名称已存在");
        }
        else if (table.getTableId().equals(table.getMasterId()))
        {
            return error("修改数据表'" + table.getTableName() + "'失败，上级数据表不能选择自己");
        }
        table.setUpdateBy(getUsername());
        return toAjax(tableService.updateTable(table));
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableId}")
    public AjaxResult remove(@PathVariable("tableId") Long tableId)
    {
        if (tableService.hasChildByTableId(tableId))
        {
            return warn("存在子菜单,不允许删除");
        }
        if (tableService.checkTableExistRole(tableId))
        {
            return warn("菜单已分配,不允许删除");
        }
        return toAjax(tableService.deleteTableById(tableId));
    }

}
