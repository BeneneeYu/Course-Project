package com.photoSharing.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @program: Project
 * @description: JDBC
 * @author: Shen Zhengyu
 * @create: 2020-07-13 13:57
 **/
public class JdbcUtils{
    // 链接池
    private static DataSource dataSource = null;
    static {
        dataSource = new ComboPooledDataSource();
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * DBUtils工具类对象
     */

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public static QueryRunner getQueryRunner() {
        return new QueryRunner(dataSource);
    }
}
