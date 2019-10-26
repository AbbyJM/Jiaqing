package com.abby.jiaqing.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncode {
    @Bean(name = "BCryptPasswordEncoder")
    public PasswordEncoder encode(){
        return new BCryptPasswordEncoder();
    }
}
