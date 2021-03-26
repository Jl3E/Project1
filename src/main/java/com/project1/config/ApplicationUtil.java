package com.project1.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Deprecated
public class ApplicationUtil {

    private static ApplicationUtil instance;

    private ApplicationUtil(){}

    public static ApplicationUtil getInstance(){
        if(instance == null){
            instance = new ApplicationUtil();
        }
        return instance;
    }

    final int MAX_THREADS = 4;
    ExecutorService threadActivatah = Executors.newFixedThreadPool(MAX_THREADS);

    public ExecutorService getThreadActivatah() {
        return threadActivatah;
    }

//    public Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(
//                "jdbc:postgresql://samplepsql.cd1hrpx24rhn.us-west-1.rds.amazonaws.com:5432/postgres?currentSchema=project1",
//                "joe",
//                "momma");
//    }
}
