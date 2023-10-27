package com.yuzarsif.awscognitomysql.dto;

public record CreateEmployerRequestDto(String email,
                                       String password,
                                       String companyName,
                                       String website) {
}
