package com.atguigu.book.controller;

import com.atguigu.book.pojo.Book;
import com.atguigu.book.pojo.Cart;
import com.atguigu.book.pojo.CartItem;
import com.atguigu.book.pojo.User;
import com.atguigu.book.service.CartItemService;
import com.google.gson.Gson;

import javax.servlet.http.HttpSession;

/**
 * @author Diyang Li
 * @create 2022-05-13 12:33 PM
 */
public class CartController {
    private CartItemService cartItemService;

    public String index(HttpSession session){
        User user = (User) session.getAttribute("currUser");
        Cart cart = cartItemService.getCart(user);
        user.setCart(cart);
        //重新设置session currUser的cart
        session.setAttribute("curUser", user);
        return "cart/cart";
    }
    public String addCart(Integer bookId, HttpSession session){
        User user = (User) session.getAttribute("currUser");

        //如果说没有这本书，cartItemDAO就会直接读取buycount = 1，添加到数据库中
        CartItem cartItem = new CartItem(new Book(bookId), 1, user);
        cartItemService.addOrUpdateCartItem(cartItem, user.getCart());
        //add操作完后，要重新读取cart，再加载到cart页面
        return "redirect:cart.do";
    }


    public String editCart(Integer cartItemId, Integer buyCount){
        cartItemService.updateCartItem(new CartItem(cartItemId, buyCount));
        return "redirect:cart.do";
    }

    public String cartInfo(HttpSession session){
        User user =(User)session.getAttribute("currUser");
        Cart cart = cartItemService.getCart(user);

        //调用Cart中的三个属性的get方法，目的是在此处计算这三个属性的值，否则这三个属性为null，
        //导致的结果就是下一步的gson转化时，为null的属性会被忽略
        cart.getTotalBookCount();
        cart.getTotalCount();
        cart.getTotalMoney();

        Gson gson = new Gson();
        String cartJsonStr = gson.toJson(cart);
        return "json:"+cartJsonStr ;
    }
}
