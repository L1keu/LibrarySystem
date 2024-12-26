package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.BorrowRecord;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    // 获取书籍详情
    public Book getBookById(String id) {
        return bookRepository.findById(id).orElse(null);
    }
    // 借阅书籍
    @Transactional
    public boolean borrowBook(String bookId, String userId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null && book.getStock() > 0) {
            book.setStock(book.getStock() - 1);
            bookRepository.save(book);
            BorrowRecord record = new BorrowRecord();
            record.setUserId(userId);
            record.setBookId(bookId);
            record.setBorrowDate(String.valueOf(System.currentTimeMillis()));
            borrowRecordRepository.save(record);
            return true;
        }
        return false;
    }
}

