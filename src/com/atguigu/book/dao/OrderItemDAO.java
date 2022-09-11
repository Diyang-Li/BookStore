package com.atguigu.book.dao;

import com.atguigu.book.pojo.OrderBean;
import com.atguigu.book.pojo.OrderItem;

/**
 * @author Diyang Li
 * @create 2022-05-16 10:18 AM
 */
public interface OrderItemDAO {
    void addOrderItem(OrderItem orderItem);
}
