package com.yuzarsif.awscognitomysql.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/employer")
    public String employer() {
        return "EMPLOYER";
    }

    @GetMapping("/employee")
    public String employee() {
        return "EMPLOYEE";
    }
}
