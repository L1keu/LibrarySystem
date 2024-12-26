package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    // 获取当前用户信息
    @GetMapping("/getUserInfo")
    public Response getUserInfo(@RequestParam String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new Response(false, "获取用户信息失败");
        }
        return new Response(true, user);
    }
    // 更新用户信息
    @PostMapping("/updateUserInfo")
    public Response updateUserInfo(@RequestParam String userId, @RequestBody User user) {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser == null) {
            return new Response(false, "用户不存在");
        }
        // 更新用户信息
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setPassword(user.getPassword());
        userRepository.save(existingUser);
        return new Response(true, "用户信息更新成功");
    }
    // 响应结构
    public static class Response {
        private boolean success;
        private String message;
        private Object data;
        public Response(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        public Response(boolean success, Object data) {
            this.success = success;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
        // Getters and Setters
    }
}

