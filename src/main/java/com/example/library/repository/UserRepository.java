package com.example.library.repository;

import com.example.library.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);  // 通过用户名查询
}


