package com.project1.config;

import com.zaxxer.hikari.HikariConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
//    private static HikariConfig config = new HikariConfig();
//    private static HikariCPDataSource ds;
//
//    static {
//        config.setJdbcUrl("jdbc:postgresql://samplepsql.cd1hrpx24rhn.us-west-1.rds.amazonaws.com:5432/postgres?currentSchema=project1");
//        config.setUsername("joe");
//        config.setPassword("momma");
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//        ds = new HikariCPDataSource(config);
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return ds.getConnection();
//    }
//
//    private HikariCPDataSource(HikariConfig config){}

    private static ConnectionUtil instance;

    private ConnectionUtil(){}

    public static ConnectionUtil getInstance(){
        if(instance == null){
            instance = new ConnectionUtil();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://samplepsql.cd1hrpx24rhn.us-west-1.rds.amazonaws.com:5432/postgres?currentSchema=project1",
                "joe",
                "momma");
    }
}
