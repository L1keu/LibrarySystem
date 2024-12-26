package com.example.library.controller;

import com.example.library.model.BorrowRecord;
import com.example.library.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BorrowRecordController {
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    // 获取借阅记录
    @GetMapping("/borrowRecords")
    public List<BorrowRecord> getBorrowRecords() {
        // 获取所有借阅记录
        List<BorrowRecord> records = borrowRecordRepository.findAll();
        records.forEach(record -> {
            boolean isOverdue = LocalDate.now().isAfter(LocalDate.parse(record.getReturnDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(30));
            record.setOverdue(isOverdue);
            // 计算罚金
            double fine = calculateFine(record.getBorrowDate(), record.getReturnDate(), 100);
            record.setFine(fine);
        });
        return records;
    }

    // 归还图书
    @PostMapping("/returnBook")
    public Response returnBook(@RequestBody BorrowRecord request) {
        BorrowRecord record = borrowRecordRepository.findById(request.getId()).orElse(null);
        if (record != null) {
            record.setReturnDate(LocalDate.now().toString());
            borrowRecordRepository.save(record);
            return new Response(true, "归还成功！");
        }
        return new Response(false, "记录不存在");
    }

    // 续借图书
    @PostMapping("/renewBook")
    public Response renewBook(@RequestBody BorrowRecord request) {
        BorrowRecord record = borrowRecordRepository.findById(request.getId()).orElse(null);
        if (record != null && record.getReturnDate() == null) {
            // 假设续借成功
            return new Response(true, "续借成功！");
        }
        return new Response(false, "图书无法续借");
    }

    // 计算罚金
    private double calculateFine(String borrowDate, String returnDate, double price) {
        if (returnDate == null || returnDate.isEmpty()) return 0;
        LocalDate borrowLocalDate = LocalDate.parse(borrowDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate returnLocalDate = LocalDate.parse(returnDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        long daysOverdue = returnLocalDate.toEpochDay() - borrowLocalDate.plusDays(30).toEpochDay();
        if (daysOverdue > 0) {
            return Math.min(price * 0.005 * daysOverdue, price);
        }
        return 0;
    }

    // 响应结构
    public static class Response {
        private boolean success;
        private String message;

        public Response(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
        // Getters and Setters
    }
}


