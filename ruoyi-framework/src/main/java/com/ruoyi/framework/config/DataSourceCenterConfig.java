package com.ruoyi.framework.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.framework.datasource.DataSourceModel;
import com.ruoyi.system.domain.DataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.HashMap;
import java.util.Map;

public class DataSourceCenterConfig {

    @Bean(name = "centerDataSource")
    public DataSourceModel initCenterDataSource() {
        DataSourceModel data = new DataSourceModel();
        DruidDataSource ds1 = data.createDataSource("com.mysql.cj.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/ry?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull",
        "root", "root");
        DruidDataSource ds2 = data.createDataSource("com.mysql.cj.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/ry?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull",
        "root", "root");
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER.name(), ds1);
        targetDataSources.put(DataSourceType.SLAVE.name(), ds2);
        data.setTargetDataSources(targetDataSources);
        data.setDefaultTargetDataSource(ds1);
        return data;
    }

    @Bean(name = "test1SqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier(value = "centerDataSource") DataSourceModel dataSource) throws Exception {
        SqlSessionFactoryBean bean = new PackagesSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        Interceptor[] interceptor = {new PaginationInterceptor()};
        bean.setPlugins(interceptor); //分页插件
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] configXml = resolver.getResources("classpath*:mapper/**/*Mapper.xml");
        bean.setMapperLocations(configXml);
        bean.setConfigLocation(resolver.getResource("classpath:mapper/mybatis-config.xml"));
        bean.setTypeAliasesPackage("com.ruoyi.*.domain"); //自定义解析
        return bean.getObject();
    }

    @Bean(name = "test1TransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("centerDataSource") DataSourceModel dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "test1SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("test1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
