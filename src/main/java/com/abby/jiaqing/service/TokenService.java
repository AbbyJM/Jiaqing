package com.abby.jiaqing.service;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {
    boolean validate(HttpServletRequest request);
}
