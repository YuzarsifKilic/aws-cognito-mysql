package com.yuzarsif.awscognitomysql.dto;

import com.yuzarsif.awscognitomysql.model.Employee;
import lombok.Data;

public record EmployeeDto(String id,
                          String email,
                          String firstName,
                          String lastName) {

    public static EmployeeDto convert(Employee from) {
        return new EmployeeDto(
                from.getId(),
                from.getEmail(),
                from.getFirstName(),
                from.getLastName());
    }


}
