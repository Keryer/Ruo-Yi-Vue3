package com.ruoyi.system.service;

import com.ruoyi.system.domain.DatahouseProject;

import java.util.List;

/**
 * 项目信息 服务层
 *
 * @author Kery
 */
public interface ISysProjectService {

    /**
     * 新增项目对象
     *
     * @param project 项目对象
     * @return
     */
    public int insertProject(DatahouseProject project);

    /**
     * 查找项目集合
     *
     * @param project 项目对象
     * @return 项目集合
     */
    public List<DatahouseProject> selectProjectList(DatahouseProject project);

    /**
     * 批量删除项目
     *
     * @param projectIds 需要删除的项目ID集合
     * @return 结果
     */
    public int deleteProjectByIds(Long[] projectIds);

    /**
     * 根据ID查询单个项目信息
     *
     * @param projectId 需要查询的项目ID
     * @return 项目对象
     */
    public DatahouseProject selectProjectById(Long projectId);

    public int updateProject(DatahouseProject project);
}
