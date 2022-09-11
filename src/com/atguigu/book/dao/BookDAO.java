package com.atguigu.book.dao;

import com.atguigu.book.pojo.Book;

import java.util.List;

/**
 * @author Diyang Li
 * @create 2022-05-12 12:30 PM
 */
public interface BookDAO {
    List<Book> getBookList();
    Book getBook(Integer id);
}
