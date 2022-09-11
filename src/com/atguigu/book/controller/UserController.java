package com.atguigu.book.controller;

import com.atguigu.book.pojo.Cart;
import com.atguigu.book.pojo.User;
import com.atguigu.book.service.CartItemService;
import com.atguigu.book.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class UserController {

    private UserService userService;
    private CartItemService cartItemService;

    public String login(String uname, String pwd, HttpSession session) {

        User user = userService.login(uname, pwd);
        if (user != null) {
            Cart cart = cartItemService.getCart(user);
            user.setCart(cart);
            session.setAttribute("currUser", user);
            return "redirect:book.do";
        }
        return "user/login";
    }

    public String regist(String uname, String pwd, String email, String verifyCode, HttpSession session, HttpServletResponse resp) throws IOException {
        Object kaptcha_session_key = session.getAttribute("KAPTCHA_SESSION_KEY");
        if (kaptcha_session_key == null || !verifyCode.equals(kaptcha_session_key)) {
            //注意这个名字，dispatcherServlet中如果是resp，如果这里写response，找不到这个argument
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            //out.println("<script language='javascript'>alert('验证码不正确！');window.location.href='page.do?operate=page&page=user/regist';</script>");
            out.println("<script language='javascript'>alert('Verify code wrong！');</script>");
            return "user/regist";
        } else {
            if (verifyCode.equals(kaptcha_session_key)) {
                userService.addUser(new User(uname, pwd, email, 0));
                return "user/login";
            }
        }
        return "user/login";
    }

    public String ckUname(String uname){
        User user = userService.getUser(uname);

        if (user!=null){
//            用户名已经占用
            return "json:{'uname': '1'}";
        }else{
//            可以注册
            return "json:{'uname': '0'}";

        }
    }
}
