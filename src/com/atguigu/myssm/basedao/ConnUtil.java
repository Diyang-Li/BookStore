package com.atguigu.myssm.basedao;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Diyang Li
 * @create 2022-04-18 9:48 AM
 */
public class ConnUtil {
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    /*public static String DRIVER;
    public static String URL;
    public static String USER;
    public static String PWD;*/
    static Properties properties = new Properties();

    static {

        InputStream is = ConnUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");

        try {
            properties.load(is);
            /*DRIVER = properties.getProperty("jdbc.driver");
            URL = properties.getProperty("jdbc.url");
            USER = properties.getProperty("jdbc.user");
            PWD = properties.getProperty("jdbc.pwd");*/


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection createConn(){
        try {
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            /*DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName(DRIVER);
            druidDataSource.setUrl(URL);
            druidDataSource.setUsername(USER);
            druidDataSource.setPassword(PWD);*/


            return dataSource.getConnection();

            /*//1.加载驱动
            Class.forName(DRIVER);
            //2.通过驱动管理器获取连接对象
            return DriverManager.getConnection(URL, USER, PWD);*/
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConn(){
        Connection conn = threadLocal.get();
        if (conn == null){
            conn = createConn();
            threadLocal.set(conn);
        }
        //如果threadLocal没有connection就createConn，如果有connection，那就直接调取
        return threadLocal.get();
    }

    public static void closeConne() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn == null){
            return;
        }
        if (!conn.isClosed()){
            conn.close();
//            threadLocal.set(null);
            threadLocal.remove();
        }
    }

    @Test
    public void testConnection(){
        Connection conn = ConnUtil.getConn();
        System.out.println("connection: " + conn);
    }
}
