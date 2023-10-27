package com.yuzarsif.awscognitomysql.controller;

import com.yuzarsif.awscognitomysql.dto.CreateEmployerRequestDto;
import com.yuzarsif.awscognitomysql.dto.EmployerDto;
import com.yuzarsif.awscognitomysql.service.EmployerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employers")
public class EmployerController {

    private final EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @PostMapping
    public ResponseEntity<EmployerDto> createEmployer(@RequestBody CreateEmployerRequestDto request) {
        return ResponseEntity.ok(employerService.createEmployer(request));
    }
}
