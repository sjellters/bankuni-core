package com.uni.bankuni.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.bankuni.domain.ResponseBody;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException e) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().append(mapper.writeValueAsString(
                ResponseBody.unauthorized(
                        "No se pudo autenticar al usuario",
                        request.getContextPath())));
    }
}
