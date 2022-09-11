package com.atguigu.myssm.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Diyang Li
 * @create 2022-04-17 10:30 AM
 */
@WebFilter(urlPatterns = {"*.do"}, initParams = {@WebInitParam(name = "encoding", value = "UTF-8")})
public class CharacterEncodingFilter implements Filter {
    private String encoding = "UTF-8";
    //FilterConfig作用也是获取Filter的相关配置信息
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //如果没有配置初始化参数，就使用默认值，如果@WebFilter配置了的话就是用配置的
        String encodingStr = filterConfig.getInitParameter("encoding");
        if (encodingStr!=null){
            encoding = encodingStr;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ((HttpServletRequest)servletRequest).setCharacterEncoding("UTF-8");
        //这一行要养成习惯先写上，如果忘记不dofilter，后面的code没法执行，就会出现空白页面
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
