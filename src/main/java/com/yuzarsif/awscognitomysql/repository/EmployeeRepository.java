package com.yuzarsif.awscognitomysql.repository;

import com.yuzarsif.awscognitomysql.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String>{
}
