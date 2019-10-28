package com.abby.jiaqing.service;

import com.abby.jiaqing.model.domain.User;
import javax.servlet.http.HttpServletRequest;

public interface UserService {
    /**
     * 查询用户是否在线
     * @param request 当前请求
     * @return 如果在线返回true
     */
    boolean isUserOnline(HttpServletRequest request);

    User getUserData();
}
