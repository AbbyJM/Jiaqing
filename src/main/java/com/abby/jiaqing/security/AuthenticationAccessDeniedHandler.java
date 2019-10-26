package com.abby.jiaqing.security;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException e) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer=response.getWriter();
        writer.write("{\"status\":\"error\",\"msg\":\"权限不足，请联系管理员!\"}");
        writer.flush();
        writer.close();
    }
}
