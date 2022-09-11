package com.atguigu.myssm.trans;

import com.atguigu.myssm.basedao.ConnUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Diyang Li
 * @create 2022-04-18 9:32 AM
 */
public class TransactionManager {
    //ThreadLocal 是线程本地存储，在每个线程中都创建了一个ThreadLocalMap 对象，
    // 每个线程可以访问自己内部ThreadLocalMap 对象内的value。 通过这种方式，避免资源在多线程间共享。

    //开启事务


    public static void beginTrans() throws SQLException {

        ConnUtil.createConn().setAutoCommit(false);

    }
    //提交事务
    public static void commit() throws SQLException {

        Connection conn = ConnUtil.createConn();
        conn.setAutoCommit(true);
        ConnUtil.closeConne();

    }
    //回滚事务
    public static void rollback() throws SQLException {
        Connection conn = ConnUtil.getConn();
        conn.rollback();
        ConnUtil.closeConne();
    }

}
