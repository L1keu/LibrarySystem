// 创建接口BorrowRecordRepository
package com.example.library.repository;

import com.example.library.model.BorrowRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BorrowRecordRepository extends MongoRepository<BorrowRecord, String> {
    List<BorrowRecord> findByUserId(String userId); // 根据 userId 查询借阅记录
}

