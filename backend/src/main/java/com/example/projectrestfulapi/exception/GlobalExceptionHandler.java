package com.example.projectrestfulapi.exception;

import com.example.projectrestfulapi.dto.response.FormatResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();
        if (body instanceof FormatResponseDTO) {
            return body;
        }
        FormatResponseDTO<Object> formatResponseDTO = new FormatResponseDTO<>();
        if (status <= 300) {
            formatResponseDTO.setMessage("request successfully");
        }
        formatResponseDTO.setStatus(status);
        formatResponseDTO.setData(body);
        return formatResponseDTO;
    }

    public <T> ResponseEntity<FormatResponseDTO<T>> formatException(HttpStatus status, String message) {
        FormatResponseDTO<T> formatResponseDTO = new FormatResponseDTO<>();
        formatResponseDTO.setSuccess(false);
        formatResponseDTO.setStatus(status.value());
        formatResponseDTO.setMessage(message);
        formatResponseDTO.setData(null);
        return ResponseEntity.status(status).body(formatResponseDTO);
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<FormatResponseDTO<Object>> handleInvalidException(InvalidException e) {
        NumberError error = e.getNumberError();
        HttpStatus status;

        switch (error) {
            case FORBIDDEN -> status = HttpStatus.FORBIDDEN;
            case UNAUTHORIZED, UNAUTHORIZED_EMAIL, NO_REFRESH_TOKEN, INVALID_REFRESH_TOKEN -> status = HttpStatus.UNAUTHORIZED;
            case MISING_DATA, INCORRECT_DATA, USER_NOT_FOUND, VERIFICATION, COMIC_NOT_FOUND, ACCOUNT_NOT_FOUND, FOLLOW_FAILED, UNFOLLOW_FAILED, GROUP_NOT_FOUND, DUPLICATE_ACCOUNT -> status = HttpStatus.BAD_REQUEST;
            case CONFLICT_USER, CONFLICT_EMAIL -> status = HttpStatus.CONFLICT;
            case NOT_FOUND -> status = HttpStatus.NOT_FOUND;
            default -> status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return formatException(status, error.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<FormatResponseDTO<Void>> handle400(MethodArgumentNotValidException ex) {
        String message = Optional.ofNullable(ex.getBindingResult().getFieldError())
                .map(FieldError::getDefaultMessage)
                .orElse("Invalid input");
        return formatException(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<FormatResponseDTO<Void>> handleMissingData400() {
        return formatException(HttpStatus.BAD_REQUEST, "Missing data or Incorrect data");
    }

    @ExceptionHandler(AccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<FormatResponseDTO<Void>> handle403() {
        return formatException(HttpStatus.FORBIDDEN, "Forbidden");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<FormatResponseDTO<Void>> handle404() {
        return formatException(HttpStatus.NOT_FOUND, "Resource Not Found");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<FormatResponseDTO<Void>> handle500() {
        return formatException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<FormatResponseDTO<Void>> handleWrongUsers() {
        return formatException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<FormatResponseDTO<Void>> handleRuntimeException(RuntimeException ex) {
        return formatException(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
    }

}
