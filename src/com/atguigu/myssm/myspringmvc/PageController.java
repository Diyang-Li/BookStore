package com.atguigu.myssm.myspringmvc;

/**
 * 不经过servlet，页面是没有办法render的，所以，所有页面的访问都要经过page controller
 */

public class PageController {
    public String page(String page){
        return page ;       // 比如：frames/left
    }
}
