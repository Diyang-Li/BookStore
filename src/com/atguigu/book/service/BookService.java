package com.atguigu.book.service;

import com.atguigu.book.pojo.Book;

import java.util.List;

/**
 * @author Diyang Li
 * @create 2022-05-12 12:32 PM
 */
public interface BookService {
    List<Book> getBookList();
    Book getBook(Integer id);
}
