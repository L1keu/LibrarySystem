package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private String jwtSecret = "secret_key";  // JWT 密钥
    /**
     * 注册接口
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        // 检查用户名是否已存在
        if (userRepository.findByUsername(user.getUsername()) != null) {
            response.put("success", false);
            response.put("message", "用户名已存在");
            return ResponseEntity.badRequest().body(response);
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : "user");  // 默认角色为user
        // 保存用户到数据库
        userRepository.save(user);
        response.put("success", true);
        response.put("message", "注册成功");
        return ResponseEntity.ok(response);
    }
    /**
     * 登录接口
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> response = new HashMap<>();
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        User user = userRepository.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            response.put("success", false);
            response.put("message", "用户名或密码错误");
            return ResponseEntity.badRequest().body(response);
        }
        // 生成 JWT token
        String token = Jwts.builder()
                .setSubject(user.getId())
                .claim("role", user.getRole())
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
        response.put("success", true);
        response.put("message", "登录成功");
        response.put("token", token);
        response.put("role", user.getRole());
        return ResponseEntity.ok(response);
    }
}

