package com.example.library.controller;

import com.example.library.model.BorrowRecord;
import com.example.library.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FineController {
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    // 获取用户罚金信息
    @GetMapping("/getUserFines")
    public ResponseEntity<Object> getUserFines(@RequestParam String userId) {
        List<BorrowRecord> records = borrowRecordRepository.findByUserId(userId);
        if (records == null || records.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "未找到该用户的借阅记录"));
        }
        double totalFine = records.stream().mapToDouble(BorrowRecord::getFine).sum();
        return ResponseEntity.ok(new ApiResponse(true, totalFine));
    }
    // 支付罚金
    @PostMapping("/payFine")
    public ResponseEntity<Object> payFine(@RequestBody PayFineRequest request) {
        try {
            List<BorrowRecord> records = borrowRecordRepository.findByUserId(request.getUserId());
            for (BorrowRecord record : records) {
                if (record.getFine() > 0) {
                    record.setFine(0); // 支付成功，清除罚金
                    borrowRecordRepository.save(record);
                }
            }
            return ResponseEntity.ok(new ApiResponse(true, "支付成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "支付失败"));
        }
    }
    // API 响应模型
    static class ApiResponse {
        private boolean success;
        private Object message;
        public ApiResponse(boolean success, Object message) {
            this.success = success;
            this.message = message;
        }
        public boolean isSuccess() {
            return success;
        }
        public void setSuccess(boolean success) {
            this.success = success;
        }
        public Object getMessage() {
            return message;
        }
        public void setMessage(Object message) {
            this.message = message;
        }
    }
    // 请求支付罚金的请求体
    static class PayFineRequest {
        private String userId;
        private double amount;
        public String getUserId() {
            return userId;
        }
        public void setUserId(String userId) {
            this.userId = userId;
        }
        public double getAmount() {
            return amount;
        }
        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}

