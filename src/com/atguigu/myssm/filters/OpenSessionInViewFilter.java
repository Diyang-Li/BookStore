package com.atguigu.myssm.filters;

import com.atguigu.myssm.trans.TransactionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Diyang Li
 * @create 2022-04-18 9:30 AM
 */
@WebFilter("*.do")
public class OpenSessionInViewFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            //两者是同一个connection，因为在util中，如果threadlocal中的conn=null，会create connection，但是如果有的话，都是从里面获取的
            //类似单例，如果有connection的话，就会直接调用threadLocal中唯一一个connection
            TransactionManager.beginTrans();
            System.out.println("Begin Connection... ...");
            filterChain.doFilter(servletRequest, servletResponse);
            TransactionManager.commit();
            System.out.println("Commit Transaction... ...");
        }catch (Exception e){
            e.printStackTrace();
            try {
                TransactionManager.rollback();
                System.out.println("Rollback Transaction... ...");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
