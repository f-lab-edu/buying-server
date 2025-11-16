package org.example.buyingserver.common.auth;

import org.example.buyingserver.common.dto.ErrorCode;
import org.springframework.security.core.AuthenticationException;


public class JwtAuthenticationException extends AuthenticationException {

    private final ErrorCode errorCode;

    public JwtAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}