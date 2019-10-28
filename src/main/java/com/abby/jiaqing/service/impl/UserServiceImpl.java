package com.abby.jiaqing.service.impl;

import com.abby.jiaqing.mapper.UserMapper;
import com.abby.jiaqing.model.domain.User;
import com.abby.jiaqing.security.CustomUserDetails;
import com.abby.jiaqing.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private UserMapper userMapper;

    public boolean isUserOnline(HttpServletRequest request) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken);
    }

    public User getUserData(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        logger.info(authentication.toString());
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            User user=userMapper.selectByUserName(authentication.getName());
            if(user==null) {
                return null;
            }
            user.setPassword("PROTECTED");
            user.setEmail("PROTECTED");
            user.setId(-1);
            return user;
        }
        return null;
    }
}
