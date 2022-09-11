package com.atguigu.book.dao.impl;

import com.atguigu.book.dao.BookDAO;
import com.atguigu.book.pojo.Book;
import com.atguigu.myssm.basedao.BaseDAO;

import java.util.List;

/**
 * @author Diyang Li
 * @create 2022-05-12 12:31 PM
 */
public class BookDAOImpl extends BaseDAO<Book> implements BookDAO {
    @Override
    public List<Book> getBookList() {
        return super.executeQuery("SELECT * FROM `t_book` WHERE `bookStatus` = 0");
    }

    @Override
    public Book getBook(Integer id) {
        return super.load("SELECT * FROM `t_book` WHERE `id` = ?", id);
    }
}
