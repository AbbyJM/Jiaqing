package com.abby.jiaqing.web;

import com.abby.jiaqing.response.ResponseWrapper;
import com.abby.jiaqing.security.ResponseCode;
import com.abby.jiaqing.service.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping(value = "/isonline")
    public void isUserOnline(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean online=userService.isUserOnline(request);
        response.setContentType("application/json;utf-8");
        PrintWriter writer=response.getWriter();
        String responseStr;
        if(online){
            responseStr=ResponseWrapper.wrap(ResponseCode.USER_IS_ONLINE,"user is online");
        }else{
            responseStr=ResponseWrapper.wrap(ResponseCode.USER_IS_OFFLINE,"user is offline");
        }
        writer.write(responseStr);
        writer.flush();
        writer.close();
    }
}
