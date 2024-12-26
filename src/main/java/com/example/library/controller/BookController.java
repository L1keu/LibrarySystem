package com.example.library.controller;

import com.example.library.Response;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookService bookService;
    private BookRepository bookRepository;

    // 模拟数据库中的书籍数据
    private List<Book> books = new ArrayList<>();

    // 获取书籍详情
    @GetMapping("/books/{id}")
    public ResponseEntity<?> getBook(@PathVariable String id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(new Response(true, book));
        } else {
            return ResponseEntity.status(404).body(new Response(false, "Book not found"));
        }
    }

    // 搜索接口
    @GetMapping("/api/search")
    public List<Book> searchBooks(@RequestParam(required = false, defaultValue = "") String query) {
        // 如果查询为空，返回所有书籍
        if (query.isEmpty()) {
            return books;
        }
        // 按照标题、作者或ISBN过滤书籍
        return books.stream()
                .filter(book -> book.getTitle().contains(query) ||
                        book.getAuthor().contains(query) ||
                        book.getIsbn().contains(query))
                .collect(Collectors.toList());
    }

    // 借阅书籍
    @PostMapping("/borrow/{id}")
    public ResponseEntity<?> borrowBook(@PathVariable String id, @RequestHeader("Authorization") String token) {
        // 假设从 JWT 中提取用户 ID
        String userId = extractUserIdFromToken(token);  // 需要根据实际实现的 JWT 解析来编写
        boolean success = bookService.borrowBook(id, userId);
        if (success) {
            return ResponseEntity.ok(new Response(true, "Book borrowed successfully"));
        } else {
            return ResponseEntity.status(400).body(new Response(false, "Book not available"));
        }
    }

    private String extractUserIdFromToken(String token) {
        return token.substring(7);
    }

    // 获取所有图书或根据查询参数搜索
    @GetMapping
    public ResponseEntity<?> getBooks(@RequestParam(value = "query", defaultValue = "") String query) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrCategoryContainingIgnoreCase(query, query, query);
        return ResponseEntity.ok().body(books);
    }

    // 获取单本图书
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable String id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok().body(book.get());
        }
        return ResponseEntity.status(404).body("图书未找到");
    }

    // 删除图书
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            return ResponseEntity.ok().body("图书删除成功");
        }
        return ResponseEntity.status(404).body("图书未找到");
    }

    // 新增图书 API
    @PostMapping("/addBook")
    public ResponseEntity<Object> addBook(@RequestBody Book book) {
        // 检查图书是否已存在
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(false, "此图书的ISBN已存在")
            );
        }
        // 保存新的图书记录
        try {
            bookRepository.save(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "图书添加成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse(false, "新增图书失败，请稍后再试")
            );
        }
    }
}

// API 响应模型
class ApiResponse {
    private boolean success;
    private String message;
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
