package com.yuzarsif.awscognitomysql.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class CognitoUserPoolConfig {

    @Value("${aws.cognito.user-pool-id}")
    private String userPoolId;
    @Value("${aws.cognito.client-id}")
    private String clientId;
    @Value("${aws.cognito.client-secret}")
    private String clientSecret;
}
