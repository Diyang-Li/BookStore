package com.atguigu.book.service.impl;

import com.atguigu.book.dao.CartItemDAO;
import com.atguigu.book.dao.OrderDAO;
import com.atguigu.book.dao.OrderItemDAO;
import com.atguigu.book.pojo.*;
import com.atguigu.book.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Diyang Li
 * @create 2022-05-16 10:54 AM
 */
public class OrderServiceImpl implements OrderService {
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private CartItemDAO cartItemDAO;
    @Override
    public void addOrderBean(OrderBean orderBean) {
        //本来orderBeanshi 的id是null
        //所以，如果是insert操作的话直接在orderDAO中get auto-increment 的id，并设置到orderBean中
        orderDAO.addOrder(orderBean);
        Cart cart = orderBean.getOrderUser().getCart();
        Map<Integer, CartItem> cartItemMap = cart.getCartItemMap();
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartItem cartItem:cartItemMap.values()){
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setBuyCount(cartItem.getBuyCount());
            orderItem.setOrderBean(orderBean);
            orderItemDAO.addOrderItem(orderItem);
            orderItemList.add(orderItem);
        }
        orderBean.setOrderItemList(orderItemList);
        for (CartItem cartItem:cartItemMap.values()){
            cartItemDAO.delCartItem(cartItem);
        }
    }

    @Override
    public List<OrderBean> getOrderList(User user) {
        List<OrderBean> orderList = orderDAO.getOrderList(user);
        for (OrderBean orderBean: orderList){
            Integer orderTotalBookCount = orderDAO.getOrderTotalBookCount(orderBean);
            orderBean.setTotalBookCount(orderTotalBookCount);
        }

        return orderList;
    }
}
