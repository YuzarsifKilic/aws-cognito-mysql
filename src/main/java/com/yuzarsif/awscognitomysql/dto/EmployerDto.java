package com.yuzarsif.awscognitomysql.dto;

import com.yuzarsif.awscognitomysql.model.Employer;

public record EmployerDto(String id,
                          String email,
                          String companyName,
                          String website) {

    public static EmployerDto convert(Employer from) {
        return new EmployerDto(
                from.getId(),
                from.getEmail(),
                from.getCompanyName(),
                from.getWebsite());
    }
}
