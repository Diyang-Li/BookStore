package com.atguigu.book.controller;

import com.atguigu.book.pojo.Book;
import com.atguigu.book.service.BookService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Diyang Li
 * @create 2022-05-12 12:34 PM
 */
public class BookController {
    private BookService bookService ;

    public String index(HttpSession session){
        List<Book> bookList = bookService.getBookList();
        session.setAttribute("bookList",bookList);
        //加载cart
        return "index";
    }
}
