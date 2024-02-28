package com.ruoyi.framework.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.ruoyi.framework.config.DatabaseModel;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class DataSourceModel extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSource();
    }

    public DruidDataSource createDataSource(String driverClassName, String url, String username, String password) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setTestWhileIdle(true);
        dataSource.setInitialSize(5);
        dataSource.setMaxActive(15);
        dataSource.setMaxIdle(10);
        dataSource.setMinIdle(5);
        dataSource.setMaxWait(3000);
        dataSource.setTimeBetweenEvictionRunsMillis(27000);
        dataSource.setMinEvictableIdleTimeMillis(28000);
        dataSource.setLogAbandoned(true);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(600);
        dataSource.setValidationQuery("SELECT NOW() FROM DUAL");
        return dataSource;
    }

    /**
     * 设置数据源
     * 改写原有的setTargetDataSources方法，使其支持动态添加数据源
     * 根据需要的数据源类型，从相应的数据库存储表中取出数据库地址、用户名、密码等信息，动态添加数据源
     *
     * @param targetDataSource 传入的数据源
     */
    @Override
    public void setTargetDataSources(Map targetDataSource) {
        // TODO Auto-generated method stub
        Map<Object, Object> _targetDataSources = new HashMap<>();
        Set keyset = targetDataSource.keySet();
        Iterator it = keyset.iterator();
        Object key = "";
        while (it.hasNext()) {
            key = it.next();
        }
        String url = "";
        String password = "root";
        String username = "root";
        //-1表示是中转库，-2为目标库
        if(String.valueOf(key).equals("-1")){
            List<DatabaseModel> databaseList = getDatabaseList(String.valueOf(key));
            for (DatabaseModel database : databaseList) {
                try {
                    url =  database.getDatabaseUrl();
                    username = database.getUsername();
                    password = database.getPassword();
                    com.alibaba.druid.pool.DruidDataSource ds = createDataSource("com.mysql.cj.jdbc.Driver", url , username, password);
                    _targetDataSources.put(String.valueOf(database.getDatabaseId()), ds);
                } catch (Exception e) {
                    logger.error("数据库" + database.getDatabaseName() + "初始化失败");
                }
            }
            super.setTargetDataSources(_targetDataSources);
        } else if (String.valueOf(key).equals("-2")) {
            List<DatabaseModel> databaseList = getDatabaseList(String.valueOf(key));
            for (DatabaseModel database : databaseList) {
                try {
                    url = database.getDatabaseUrl();
                    username = database.getUsername();
                    password = database.getPassword();
                    com.alibaba.druid.pool.DruidDataSource ds = createDataSource("com.mysql.cj.jdbc.Driver", url , username, password);
                    _targetDataSources.put(String.valueOf(database.getDatabaseId()), ds);
                } catch (Exception e) {
                    logger.error("数据库" + database.getDatabaseName() + "初始化失败");
                }
            }
            super.setTargetDataSources(_targetDataSources);
        } else {
            super.setTargetDataSources(targetDataSource);
        }
    }


    /**
     * 获取数据库列表
     * 根据要获取的数据库类型，获取数据库列表
     * @param datasourceType
     * @return
     */
    public List<DatabaseModel> getDatabaseList(String datasourceType) {
        String url = "jdbc:mysql://127.0.0.1:3306/ry-ui?characterEncoding=utf8&autoReconnect=true";
        Connection conn = null;
        String sql;
        String user = "root";
        String pass = "root";
        String driver = "com.mysql.cj.jdbc.Driver";
        List<DatabaseModel> databaseList = new ArrayList<>();
        try {
            Class.forName(driver);
            conn = java.sql.DriverManager.getConnection(url, user, pass);
            java.sql.Statement stmt = conn.createStatement();
            if (datasourceType.equals("-1")) {
                sql = "select * from source_database where database_url != ''";
            } else {
                sql = "select * from target_database where database_url != ''";
            }
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                DatabaseModel database = new DatabaseModel();
                database.setDatabaseId(Long.parseLong(rs.getString("database_id")));
                database.setDatabaseName(rs.getString("database_name"));
                database.setDatabaseUrl(rs.getString("database_url"));
                database.setUsername(rs.getString("username"));
                database.setPassword(rs.getString("password"));
                database.setDatabaseType(rs.getString("database_type"));
                database.setCode(rs.getString("code"));
                databaseList.add(database);
            }
        } catch (SQLException e) {
            System.out.println("数据库操作出错");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return databaseList;
    }
}


