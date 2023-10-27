package com.yuzarsif.awscognitomysql.service;

import com.yuzarsif.awscognitomysql.dto.CreateEmployerRequestDto;
import com.yuzarsif.awscognitomysql.dto.EmployerDto;
import com.yuzarsif.awscognitomysql.model.Employer;
import com.yuzarsif.awscognitomysql.repository.EmployerRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployerService {

    private final EmployerRepository repository;
    private final UserService userService;

    public EmployerService(EmployerRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public EmployerDto createEmployer(CreateEmployerRequestDto request) {
        String id = userService.createUser(request.email(), request.password(), "ROLE_EMPLOYER");

        Employer employer = Employer.builder()
                .id(id)
                .email(request.email())
                .companyName(request.companyName())
                .website(request.website())
                .build();

        Employer savedEmployer = repository.save(employer);

        return EmployerDto.convert(savedEmployer);
    }
}
