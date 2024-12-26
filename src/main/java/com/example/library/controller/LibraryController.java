package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StreamUtils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LibraryController {
    @Autowired
    private UserRepository userRepository;
    // 获取系统信息：用户数、借阅量、健康状态
    @GetMapping("/systemInfo")
    public ResponseEntity<?> getSystemInfo() {
        long userCount = userRepository.count();
        int borrowCount = 350; // 这里使用硬编码的借阅量，可以从数据库获取
        String systemHealth = "正常"; // 这里默认返回正常，实际可根据健康检查获取
        return ResponseEntity.ok(new SystemInfo(userCount, borrowCount, systemHealth));
    }
    // 备份数据：将用户数据导出为 JSON 文件
    @PostMapping("/backupData")
    public ResponseEntity<?> backupData() {
        List<User> users = userRepository.findAll();
        try {
            String json = new ObjectMapper().writeValueAsString(users);
            Files.write(Paths.get("backup_users.json"), json.getBytes());
            return ResponseEntity.ok().body("数据备份成功");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("备份失败");
        }
    }
    // 恢复数据：将备份数据导入数据库
    @PostMapping("/restoreData")
    public ResponseEntity<?> restoreData() {
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get("backup_users.json"));
            List<User> users = new ObjectMapper().readValue(jsonData, new TypeReference<List<User>>(){});
            userRepository.deleteAll();
            userRepository.saveAll(users);
            return ResponseEntity.ok().body("数据恢复成功");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("恢复失败");
        }
    }
    // 修改用户角色
    @PostMapping("/changeRole")
    public ResponseEntity<?> changeRole(@RequestBody RoleChangeRequest request) {
        User user = userRepository.findById(request.getUsername()).orElse(null);
        if (user != null) {
            user.setRole(request.getNewRole());
            userRepository.save(user);
            return ResponseEntity.ok().body("角色修改成功");
        } else {
            return ResponseEntity.status(404).body("用户未找到");
        }
    }
    // 用于返回系统信息
    public static class SystemInfo {
        private long userCount;
        private int borrowCount;
        private String systemHealth;
        public SystemInfo(long userCount, int borrowCount, String systemHealth) {
            this.userCount = userCount;
            this.borrowCount = borrowCount;
            this.systemHealth = systemHealth;
        }

        public long getUserCount() {
            return userCount;
        }

        public int getBorrowCount() {
            return borrowCount;
        }

        public String getSystemHealth() {
            return systemHealth;
        }
// Getters and Setters
    }
    // 用于修改角色的请求体
    public static class RoleChangeRequest {
        private String username;
        private String newRole;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getNewRole() {
            return newRole;
        }
        public void setNewRole(String newRole) {
            this.newRole = newRole;
        }
    }
}

