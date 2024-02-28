package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.DatahouseProject;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 项目信息 数据层
 *
 * @author Kery
 */
public interface SysProjectMapper {
    /**
     * 插入项目
     *
     * @param project 项目对象
     * @return
     */
    public int insertProject(DatahouseProject project);

    /**
     * 查找项目
     *
     * @param project 项目对象
     * @return 项目对象列表
     */
    public List<DatahouseProject> selectProject(DatahouseProject project);

    /**
     * 删除项目
     *
     * @param projectIds 需要删除的项目ID列表
     * @return 结果
     */
    public int deleteProjectByIds(Long[] projectIds);

    public int updateAfterDelete(Integer projectId);

    public int updateAfter( Integer projectId);

    public DatahouseProject selectProjectById(Long projectId);

    public int updateProject(DatahouseProject project);
}
