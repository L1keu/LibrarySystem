package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description:
 * @Author: shenyu
 * @Date: 2024/12/26 22:22
 */
@Controller

public class HomeController {

    @GetMapping("/")
    public String home() {
        return "用户登录与注册.html"; // 返回逻辑视图名
    }
}
