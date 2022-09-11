package com.atguigu.book.dao.impl;

import com.atguigu.book.dao.OrderDAO;
import com.atguigu.book.pojo.OrderBean;
import com.atguigu.book.pojo.User;
import com.atguigu.myssm.basedao.BaseDAO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Diyang Li
 * @create 2022-05-16 10:16 AM
 */
public class OrderDAOImpl extends BaseDAO<OrderBean> implements OrderDAO {

    @Override
    public void addOrder(OrderBean orderBean) {
        //如果是insert操作的话直接get auto-increment 的id，并设置到orderBean中
       Integer orderBeanId= super.executeUpdate("INSERT INTO `t_order` VALUES(0,?,?,?,?,?)", orderBean.getOrderNo(), orderBean.getOrderDate(), orderBean.getOrderUser().getId(), orderBean.getOrderMoney(), orderBean.getOrderStatus());
       orderBean.setId(orderBeanId);
    }

    @Override
    public List<OrderBean> getOrderList(User user) {
        return super.executeQuery("SELECT * FROM `t_order` WHERE `orderUser` = ?", user.getId());
    }

    @Override
    public Integer getOrderTotalBookCount(OrderBean orderBean) {
        String sql = "SELECT SUM(t3.`buyCount`) AS totalBookCount, t3.`orderBean` FROM\n" +
                "(\n" +
                "SELECT t1.`id`, t2.`buyCount`, t2.`orderBean` FROM `t_order` t1 INNER JOIN `t_order_item` t2\n" +
                "ON t1.`id` = t2.`orderBean` WHERE t1.`orderUser`=?\n" +
                ") t3 WHERE t3.`orderBean` = ? GROUP BY t3.`orderBean`";
        return ((BigDecimal)executeComplexQuery(sql, orderBean.getOrderUser().getId(), orderBean.getId())[0]).intValue();
    }
}
