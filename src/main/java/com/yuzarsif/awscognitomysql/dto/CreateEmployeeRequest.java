package com.yuzarsif.awscognitomysql.dto;

public record CreateEmployeeRequest(String email,
                                    String password,
                                    String firstName,
                                    String lastName) {

}
