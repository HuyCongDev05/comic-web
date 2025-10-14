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
    public ResponseEntity<FormatResponseDTO<Object>> customException(InvalidException e) {
        if (e.getNumberError() == NumberError.FORBIDDEN) {
            return formatException(HttpStatus.FORBIDDEN, NumberError.FORBIDDEN.getMessage());
        } else if (e.getNumberError() == NumberError.UNAUTHORIZED) {
            return formatException(HttpStatus.UNAUTHORIZED, NumberError.UNAUTHORIZED.getMessage());
        } else if (e.getNumberError() == NumberError.MISING_DATA) {
            return formatException(HttpStatus.BAD_REQUEST, NumberError.MISING_DATA.getMessage());
        } else if (e.getNumberError() == NumberError.CONFLICT_USER) {
            return formatException(HttpStatus.CONFLICT, NumberError.CONFLICT_USER.getMessage());
        } else if (e.getNumberError() == NumberError.CONFLICT_EMAIL) {
            return formatException(HttpStatus.CONFLICT, NumberError.CONFLICT_EMAIL.getMessage());
        } else if (e.getNumberError() == NumberError.INCORRECT_DATA) {
            return formatException(HttpStatus.BAD_REQUEST, NumberError.INCORRECT_DATA.getMessage());
        } else if (e.getNumberError() == NumberError.USER_NOT_FOUND) {
            return formatException(HttpStatus.BAD_REQUEST, NumberError.USER_NOT_FOUND.getMessage());
        } else if (e.getNumberError() == NumberError.NOT_FOUND) {
            return formatException(HttpStatus.NOT_FOUND, NumberError.NOT_FOUND.getMessage());
        } else if (e.getNumberError() == NumberError.UNAUTHORIZED_EMAIL) {
            return formatException(HttpStatus.UNAUTHORIZED, NumberError.UNAUTHORIZED_EMAIL.getMessage());
        }
        return formatException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
