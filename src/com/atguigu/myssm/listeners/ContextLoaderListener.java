package com.atguigu.myssm.listeners;

import com.atguigu.myssm.ioc.BeanFactory;
import com.atguigu.myssm.ioc.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author Diyang Li
 * @create 2022-04-19 9:58 AM
 */
//监听上下文启动，在上下文启动的时候去创建IOC容器，然后将其保存在appliction作用域
    //后面的中央控制器再从application作用域中获取IOC容器
@WebListener
public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //1.获取ServletContext对象
        ServletContext application = sce.getServletContext();
        // //2.获取上下文的初始化参数,去web.xml配置
        String path = application.getInitParameter("contextConfigLocation");
        //3.创建IOC容器
        BeanFactory beanFactory = new ClassPathXmlApplicationContext(path);
        // 获取getServletContext, 把beanFactory保存到application作用域,
        //4.将IOC容器保存到application作用域
        application.setAttribute("beanFactory", beanFactory);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
