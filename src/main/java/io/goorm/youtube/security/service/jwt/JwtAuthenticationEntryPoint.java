package io.goorm.youtube.security.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.goorm.youtube.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("Unauthorized error: {}", authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String message = authException.getMessage();
        if (message == null) {
            message = "인증에 실패했습니다.";
        }

        ApiResponse<Void> errorResponse = ApiResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                message
        );

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}