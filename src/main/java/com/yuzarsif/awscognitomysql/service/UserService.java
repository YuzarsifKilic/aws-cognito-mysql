package com.yuzarsif.awscognitomysql.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.yuzarsif.awscognitomysql.config.CognitoUserPoolConfig;
import com.yuzarsif.awscognitomysql.dto.SignInResponseDto;
import com.yuzarsif.awscognitomysql.exception.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final AWSCognitoIdentityProvider awsCognitoIdentityProvider;
    private final CognitoUserPoolConfig userPoolConfig;

    public UserService(AWSCognitoIdentityProvider awsCognitoIdentityProvider, CognitoUserPoolConfig userPoolConfig) {
        this.awsCognitoIdentityProvider = awsCognitoIdentityProvider;
        this.userPoolConfig = userPoolConfig;
    }

    public String createUser(String email, String password, String role) {
        try {
            AttributeType emailAttribute = new AttributeType()
                    .withName("email").withValue(email);

            AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest()
                    .withUserPoolId(userPoolConfig.getUserPoolId())
                    .withUsername(email)
                    .withTemporaryPassword(password)
                    .withUserAttributes(emailAttribute)
                    .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL);

            AdminCreateUserResult createUserResult = awsCognitoIdentityProvider.adminCreateUser(createUserRequest);

            AdminSetUserPasswordRequest setUserPasswordRequest = new AdminSetUserPasswordRequest()
                    .withUsername(email)
                    .withUserPoolId(userPoolConfig.getUserPoolId())
                    .withPassword(password)
                    .withPermanent(true);

            awsCognitoIdentityProvider.adminSetUserPassword(setUserPasswordRequest);

            AdminAddUserToGroupRequest addUserToGroupRequest =new AdminAddUserToGroupRequest()
                    .withGroupName(role)
                    .withUserPoolId(userPoolConfig.getUserPoolId())
                    .withUsername(email);

            awsCognitoIdentityProvider.adminAddUserToGroup(addUserToGroupRequest);

            return createUserResult.getUser().getUsername();
        } catch (Exception e) {
            throw new AuthenticationException("Error during sign up : " + e.getMessage());
        }
    }

    public SignInResponseDto signIn(String email, String password) {
        SignInResponseDto signInResponseDto = new SignInResponseDto();

        final Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", email);
        authParams.put("PASSWORD", password);
        authParams.put("SECRET_HASH", calculateSecretHash(userPoolConfig.getClientId(), userPoolConfig.getClientSecret(), email));

        final AdminInitiateAuthRequest initiateAuthRequest = new AdminInitiateAuthRequest();
        initiateAuthRequest
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withClientId(userPoolConfig.getClientId())
                .withUserPoolId(userPoolConfig.getUserPoolId())
                .withAuthParameters(authParams);

        try {
            AdminInitiateAuthResult initiateAuthResult = awsCognitoIdentityProvider.adminInitiateAuth(initiateAuthRequest);

            AuthenticationResultType authenticationResultType = null;

            if (initiateAuthResult.getChallengeName() != null && !initiateAuthResult.getChallengeName().isEmpty()) {
                if (initiateAuthResult.getChallengeName().contentEquals("NEW_PASSWORD_REQUIRED")) {
                    if (password == null) {
                        throw new AuthenticationException("User must change password " + initiateAuthResult.getChallengeName());
                    } else {
                        final Map<String, String> challengeResponses = new HashMap<>();
                        challengeResponses.put("USERNAME", email);
                        challengeResponses.put("PASSWORD", password);

                        challengeResponses.put("NEW_PASSWORD", password);
                        authParams.put("SECRET_HASH", calculateSecretHash(userPoolConfig.getClientId(), userPoolConfig.getClientSecret(), email));

                        final AdminRespondToAuthChallengeRequest adminRespondToAuthChallengeRequest = new AdminRespondToAuthChallengeRequest()
                                .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                                .withChallengeResponses(challengeResponses)
                                .withClientId(userPoolConfig.getClientId())
                                .withUserPoolId(userPoolConfig.getUserPoolId())
                                .withSession(initiateAuthResult.getSession());

                        AdminRespondToAuthChallengeResult adminRespondToAuthChallengeResult = awsCognitoIdentityProvider.adminRespondToAuthChallenge(adminRespondToAuthChallengeRequest);
                        authenticationResultType = adminRespondToAuthChallengeResult.getAuthenticationResult();

                        signInResponseDto.setAccessToken(authenticationResultType.getAccessToken());
                        signInResponseDto.setIdToken(authenticationResultType.getIdToken());
                        signInResponseDto.setRefreshToken(authenticationResultType.getRefreshToken());
                        signInResponseDto.setExpiresIn(authenticationResultType.getExpiresIn());
                        signInResponseDto.setTokenType(authenticationResultType.getTokenType());
                    }
                } else {
                    throw new AuthenticationException("User has other challenge " + initiateAuthResult.getChallengeName());
                }
            } else {
                System.out.println("User has no challenge");
                authenticationResultType = initiateAuthResult.getAuthenticationResult();

                signInResponseDto.setAccessToken(authenticationResultType.getAccessToken());
                signInResponseDto.setIdToken(authenticationResultType.getIdToken());
                signInResponseDto.setRefreshToken(authenticationResultType.getRefreshToken());
                signInResponseDto.setExpiresIn(authenticationResultType.getExpiresIn());
                signInResponseDto.setTokenType(authenticationResultType.getTokenType());
            }
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }

        return signInResponseDto;
    }

    public void signOut(String accessToken) {
        awsCognitoIdentityProvider.globalSignOut(new GlobalSignOutRequest().withAccessToken(accessToken));
    }

    private String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String userName) {
        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

        SecretKeySpec signingKey = new SecretKeySpec(
                userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new AuthenticationException("Error while calculating ");
        }
    }
}
