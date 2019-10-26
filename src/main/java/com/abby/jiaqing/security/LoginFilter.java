package com.abby.jiaqing.security;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public class LoginFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
    }
}
