package com.abby.jiaqing.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
    private String userName;
    private String password;
    private boolean isAccountNotLocked;
    private List<SimpleGrantedAuthority> authorities;

    public CustomUserDetails(String name,String password,boolean lock,String[] roles){
        userName=name;
        this.password=password;
        isAccountNotLocked=!lock;
        authorities=new ArrayList<SimpleGrantedAuthority>();
        for(String role:roles){
            authorities.add(new SimpleGrantedAuthority(role));
        }
    }

    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return userName;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return isAccountNotLocked;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return isAccountNotLocked;
    }
}
