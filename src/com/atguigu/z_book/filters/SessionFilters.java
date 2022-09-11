package com.atguigu.z_book.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Diyang Li
 * @create 2022-05-17 3:21 PM
 */
//@WebFilter(urlPatterns = {"*.do", "*.html"},
//        initParams = {
//                @WebInitParam(name = "white",
//                        value = "/proj27/page.do?operate=page&page=user/login,/proj27/user.do?null," +
//                                "/proj27/page.do?operate=page&page=user/regist")
//        })
public class SessionFilters implements Filter {
    List<String> whiteList = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String white = filterConfig.getInitParameter("white");
        String[] whiteArr = white.split(",");
        whiteList = Arrays.asList(whiteArr);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        System.out.println("uri: " + request.getRequestURI());
        System.out.println("queryString: " + request.getQueryString());
        //这里的print是为了做参考
        ///proj25/page.do
        String uri = request.getRequestURI();
        //operate=page&page=user/login
        String queryString = request.getQueryString();
        String str = uri + "?" + queryString;
        if (whiteList.contains(str)){
            filterChain.doFilter(request, response);
        }else{
            HttpSession session = request.getSession();
            Object currObj = session.getAttribute("currUser");
            if (currObj == null) {
                response.sendRedirect("page.do?operate=page&page=user/login");
            } else {
                filterChain.doFilter(request, response);
            }
        }


    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
