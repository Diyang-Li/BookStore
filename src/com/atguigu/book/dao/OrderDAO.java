package com.atguigu.book.dao;

import com.atguigu.book.pojo.OrderBean;
import com.atguigu.book.pojo.User;

import java.util.List;

/**
 * @author Diyang Li
 * @create 2022-05-16 10:16 AM
 */
public interface OrderDAO {
    void addOrder(OrderBean orderBean);
    List<OrderBean> getOrderList(User user);
    Integer getOrderTotalBookCount(OrderBean orderBean);
}
