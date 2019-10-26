package com.abby.jiaqing.security;

import com.abby.jiaqing.response.ResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginFailedHandler implements AuthenticationFailureHandler {
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException e) throws IOException, ServletException {
        response.setContentType("application/json;utf-8");
        String responseStr= ResponseWrapper.wrap(ResponseCode.BAD_CREDENTIALS,"bad credentials,login failed");
        PrintWriter writer=response.getWriter();
        writer.write(responseStr);
        writer.flush();
        writer.close();
    }
}
