package com.abby.jiaqing.security;

import com.abby.jiaqing.response.ResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 自定义用户未登录时的行为
 * 直接给前端返回用户未登录的json
 */

@Component
public class DefaultLoginUrlAuthEntryPoint extends LoginUrlAuthenticationEntryPoint {
    /**
     * @param loginFormUrl URL where the login page can be found. Should either be relative to the web-app context path
     *                     (include a leading {@code /}) or an absolute URL.
     */
    public DefaultLoginUrlAuthEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    public DefaultLoginUrlAuthEntryPoint(){
        super("/doNotNeed");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setContentType("application/json;utf-8");
        String responseStr= ResponseWrapper.wrap(ResponseCode.USER_NOT_LOGGED_IN,"user is not logged in");
        PrintWriter writer=response.getWriter();
        writer.write(responseStr);
        writer.flush();
        writer.close();
    }
}
