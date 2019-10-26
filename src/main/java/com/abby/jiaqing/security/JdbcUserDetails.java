package com.abby.jiaqing.security;

import com.abby.jiaqing.mapper.UserMapper;
import com.abby.jiaqing.model.domain.User;
import javax.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JdbcUserDetails implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userMapper.selectByUserName(username);
        if(user==null){
            throw new UsernameNotFoundException("user name "+username+" not found");
        }
        String[] roles=user.getRoles().split(",");
        return new CustomUserDetails(user.getUsername(),user.getPassword(),!user.getActive(),roles);
    }
}
