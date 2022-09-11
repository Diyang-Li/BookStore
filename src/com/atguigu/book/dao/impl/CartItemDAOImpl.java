package com.atguigu.book.dao.impl;

import com.atguigu.book.dao.CartItemDAO;
import com.atguigu.book.pojo.CartItem;
import com.atguigu.book.pojo.User;
import com.atguigu.myssm.basedao.BaseDAO;

import java.util.List;

/**
 * @author Diyang Li
 * @create 2022-05-13 12:38 PM
 */
public class CartItemDAOImpl extends BaseDAO<CartItem> implements CartItemDAO {

    @Override
    public void addCartItem(CartItem cartItem) {
        super.executeUpdate("INSERT INTO `t_cart_item` VALUES(0,?,?,?)", cartItem.getBook().getId(), cartItem.getBuyCount(), cartItem.getUserBean().getId());
    }

    @Override
    public void updateCartItem(CartItem cartItem) {
        super.executeUpdate("UPDATE `t_cart_item` SET `buyCount` = ? WHERE `id` = ?", cartItem.getBuyCount(), cartItem.getId());
    }

    @Override
    public List<CartItem> getCartItemList(User user) {
        return super.executeQuery("SELECT * FROM `t_cart_item` WHERE `userBean` = ?", user.getId());
    }

    @Override
    public void delCartItem(CartItem cartItem) {
        super.executeUpdate("DELETE FROM `t_cart_item` WHERE `id` = ?", cartItem.getId());
    }
}
