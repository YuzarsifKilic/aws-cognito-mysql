package com.yuzarsif.awscognitomysql.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDetails {

    private LocalDateTime timestamp;
    private String message;
    private String path;
}
