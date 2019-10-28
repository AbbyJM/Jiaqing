package com.abby.jiaqing.service.impl;

import com.abby.jiaqing.mapper.UserMapper;
import com.abby.jiaqing.model.domain.User;
import com.abby.jiaqing.service.UserService;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    public boolean isUserOnline(HttpServletRequest request) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken);
    }

    public User getUserData(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            UserDetails userDetails=(UserDetails)authentication.getPrincipal();
            String name=userDetails.getUsername();
            User user=userMapper.selectByUserName(name);
            user.setPassword("PROTECTED");
            user.setEmail("PROTECTED");
            user.setId(-1);
            return user;
        }
        return null;
    }
}
