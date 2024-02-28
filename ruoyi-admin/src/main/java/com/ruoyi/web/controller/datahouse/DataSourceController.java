package com.ruoyi.web.controller.datahouse;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.DataSource;
import com.ruoyi.system.service.ISysDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/datahouse/datasource")
public class DataSourceController extends BaseController
{
    @Autowired
    private ISysDataSourceService dataSourceService;

    @PreAuthorize("@ss.hasPermi('datahouse:datasource:list')")
    @GetMapping("/list")
    public List<DataSource> list(DataSource source)
    {
        if(Objects.equals(source.getDatasourceType(), "source"))
            source.setDatasourceType("source_database");
        else
            source.setDatasourceType("target_database");
        return dataSourceService.selectSource(source);
    }

    @PreAuthorize("@ss.hasPermi('datahouse:datasource:add')")
    @GetMapping("/add")
    public void add(DataSource source)
    {
        if(Objects.equals(source.getDatasourceType(), "source"))
            source.setDatasourceType("source_database");
        else
            source.setDatasourceType("target_database");
        dataSourceService.insertSource(source);
    }
}
