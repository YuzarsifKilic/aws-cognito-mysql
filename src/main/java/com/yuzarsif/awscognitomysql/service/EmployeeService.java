package com.yuzarsif.awscognitomysql.service;

import com.yuzarsif.awscognitomysql.dto.CreateEmployeeRequest;
import com.yuzarsif.awscognitomysql.dto.EmployeeDto;
import com.yuzarsif.awscognitomysql.model.Employee;
import com.yuzarsif.awscognitomysql.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final UserService userService;

    public EmployeeService(EmployeeRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public EmployeeDto createEmployee(CreateEmployeeRequest request) {
        String id = userService.createUser(request.email(), request.password(), "ROLE_EMPLOYEE");

        Employee employee = Employee.builder()
                .id(id)
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        Employee savedEmployee = repository.save(employee);

        return EmployeeDto.convert(savedEmployee);
    }
}
