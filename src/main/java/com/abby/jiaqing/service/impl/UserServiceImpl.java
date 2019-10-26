package com.abby.jiaqing.service.impl;

import com.abby.jiaqing.service.UserService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    public boolean isUserOnline(HttpServletRequest request) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken);
    }
}
