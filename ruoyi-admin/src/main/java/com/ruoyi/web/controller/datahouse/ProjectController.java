package com.ruoyi.web.controller.datahouse;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.DatahouseProject;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/datahouse/project")
public class ProjectController extends BaseController {

    @Autowired
    private ISysProjectService projectService;

    @PreAuthorize("@ss.hasPermi('datahouse:project:list')")
    @GetMapping("/list")
    public List<DatahouseProject> list(DatahouseProject project){
        System.out.println(project);
        return projectService.selectProjectList(project);
    }

    @Log(title = "项目", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('datahouse:project:delete')")
    @DeleteMapping("/{projIds}")
    public AjaxResult remove(@PathVariable Long[] projIds){
        return toAjax(projectService.deleteProjectByIds(projIds));
    }

    @PreAuthorize("@ss.hasPermi('datahouse:project:add')")
    @Log(title = "项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody DatahouseProject project){
        return toAjax(projectService.insertProject(project));
    }

    @PreAuthorize("@ss.hasPermi('datahouse:project:query')")
    @GetMapping(value = "/{projectId}")
    public AjaxResult getProject(@PathVariable Long projectId) {
        return success(projectService.selectProjectById(projectId));
    }

    /**
     * 修改项目
     */
    @PreAuthorize("@ss.hasPermi('datahouse:project:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody DatahouseProject project)
    {
        return toAjax(projectService.updateProject(project));
    }
}
