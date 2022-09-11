package com.atguigu.book.controller;

import com.atguigu.book.pojo.OrderBean;
import com.atguigu.book.pojo.User;
import com.atguigu.book.service.OrderService;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Diyang Li
 * @create 2022-05-16 11:53 AM
 */
public class OrderController {
    private OrderService orderService;

    public String checkout(HttpSession session){
        OrderBean orderBean = new OrderBean();
        Date now = new Date();
        User user = (User) session.getAttribute("currUser");

        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dateFormate.format(now).replaceAll("[[\\s-:punct:]]","");
        orderBean.setOrderNo(UUID.randomUUID().toString() + "_" + dateString);
        orderBean.setOrderDate(now);
        orderBean.setOrderUser(user);
        orderBean.setOrderMoney(user.getCart().getTotalMoney());
        orderBean.setOrderStatus(0);

        orderService.addOrderBean(orderBean);
        return "index";
    }

    public String getOrderList(HttpSession session){
        User user = (User) session.getAttribute("currUser");
        List<OrderBean> orderList = orderService.getOrderList(user);
        user.setOrderList(orderList);
        session.setAttribute("currUser", user);
        return "order/order";
    }

}
