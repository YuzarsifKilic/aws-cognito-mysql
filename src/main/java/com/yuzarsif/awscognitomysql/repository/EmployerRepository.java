package com.yuzarsif.awscognitomysql.repository;

import com.yuzarsif.awscognitomysql.model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, String>{
}
