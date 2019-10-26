package com.abby.jiaqing.security;

import javax.annotation.Resource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private AuthenticationAccessDeniedHandler accessDeniedHandler;

    @Resource
    private LogoutHandler logoutSuccessHandler;

    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    @Resource
    private LoginFailedHandler loginFailedHandler;

    @Resource
    private JdbcUserDetails jdbcUserDetailsService;

    @Resource
    private DefaultLoginUrlAuthEntryPoint loginUrlAuthEntryPoint;

    @Resource(name = "BCryptPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder authentication) throws Exception {
        authentication.userDetailsService(jdbcUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity webSecurity){
        webSecurity
            .ignoring()
            .antMatchers("/index.html","/static/**");
    }
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeRequests()
            .antMatchers("/test/**").permitAll()
            .antMatchers("/user/isonline**").permitAll()
            .antMatchers("/token/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginProcessingUrl("/login")
            .permitAll()
            .failureHandler(loginFailedHandler)
            .successHandler(loginSuccessHandler)
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessHandler(logoutSuccessHandler)
            .permitAll()
            .and()
            .csrf()
            .disable()
            .exceptionHandling()
            .authenticationEntryPoint(loginUrlAuthEntryPoint)
            .accessDeniedHandler(accessDeniedHandler);
    }
}
