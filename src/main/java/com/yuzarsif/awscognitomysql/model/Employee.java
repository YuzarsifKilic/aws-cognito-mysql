package com.yuzarsif.awscognitomysql.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    private String id;
    private String email;
    private String firstName;
    private String lastName;
}
