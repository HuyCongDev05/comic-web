package com.example.projectrestfulapi.util.ResponseUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", false);
        body.put("status", status);
        body.put("message", message);
        body.put("data", null);
        body.put("timestamp", Instant.now().toString());

        response.getWriter().write(mapper.writeValueAsString(body));
        response.getWriter().flush();
    }
}
