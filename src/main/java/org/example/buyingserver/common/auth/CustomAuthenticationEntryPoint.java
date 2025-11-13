package org.example.buyingserver.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.common.dto.ApiResponse;
import org.example.buyingserver.common.dto.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        log.error("Not Authenticated Request", authException);

        ErrorCode errorCode =
                (authException instanceof JwtAuthenticationException jwtEx)
                        ? jwtEx.getErrorCode()
                        : org.example.buyingserver.common.dto.ErrorCodeAndMessage.UNAUTHORIZED;

        ApiResponse<?> apiResponse = ApiResponse.error(errorCode);
        String responseBody = objectMapper.writeValueAsString(apiResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getCode());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}