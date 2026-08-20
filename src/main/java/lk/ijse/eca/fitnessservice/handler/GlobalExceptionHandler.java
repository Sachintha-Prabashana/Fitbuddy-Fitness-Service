package lk.ijse.eca.fitnessservice.handler;

import jakarta.servlet.http.HttpServletRequest;
import lk.ijse.eca.fitnessservice.dto.ApiResponse;
import lk.ijse.eca.fitnessservice.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                ApiResponse.<Void>builder()
                        .success(false)
                        .data(ApiResponse.DataWrapper.<Void>builder()
                                .message("Resource Not Found")
                                .error(ex.getMessage())
                                .build())
                        .status(HttpStatus.NOT_FOUND.value())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now().toString())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(
                ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .data(ApiResponse.DataWrapper.<Map<String, String>>builder()
                                .message("Validation Failed")
                                .error(errors.toString())
                                .build())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now().toString())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                ApiResponse.<Void>builder()
                        .success(false)
                        .data(ApiResponse.DataWrapper.<Void>builder()
                                .message("Internal Server Error")
                                .error(ex.getMessage())
                                .build())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now().toString())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
