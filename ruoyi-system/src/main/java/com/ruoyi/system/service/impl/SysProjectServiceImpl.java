package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.DatahouseProject;
import com.ruoyi.system.mapper.SysProjectMapper;
import com.ruoyi.system.service.ISysProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目信息 服务层处理
 *
 * @author Kery
 */
@Service
public class SysProjectServiceImpl implements ISysProjectService {

    @Autowired
    private SysProjectMapper projectMapper;
    @Override
    public int insertProject(DatahouseProject project) {
        return projectMapper.insertProject(project);
    }

    @Override
    public List<DatahouseProject> selectProjectList(DatahouseProject project) {
        return projectMapper.selectProject(project);
    }

    @Override
    public int deleteProjectByIds(Long[] projectIds) {
        int ret =  projectMapper.deleteProjectByIds(projectIds);
        Long minId = 999999L;
        int index = 0;
        for(int i = 0; i < projectIds.length; i++){
            if(projectIds[i] < minId){
                minId = projectIds[i];
                index = i;
            }
        }
        for(int i = 0; i < projectIds.length; i++){
            projectMapper.updateAfterDelete(projectIds[index].intValue());
            projectMapper.updateAfter(projectIds[index].intValue());
        }
        return ret;
    }

    @Override
    public DatahouseProject selectProjectById(Long projectId)
    {
        return projectMapper.selectProjectById(projectId);
    }

    @Override
    public int updateProject(DatahouseProject project){
        return projectMapper.updateProject(project);
    }
}
