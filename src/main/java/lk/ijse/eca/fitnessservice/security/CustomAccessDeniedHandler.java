package lk.ijse.eca.fitnessservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.eca.fitnessservice.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String message = "Access Denied: You do not have permission to access this resource";

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(false)
                .data(ApiResponse.DataWrapper.<Void>builder()
                        .message(message)
                        .error("Forbidden")
                        .build())
                .status(HttpServletResponse.SC_FORBIDDEN)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now().toString())
                .build();


        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
