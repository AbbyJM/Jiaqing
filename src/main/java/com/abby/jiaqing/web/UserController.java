package com.abby.jiaqing.web;

import com.abby.jiaqing.model.domain.User;
import com.abby.jiaqing.response.ResponseWrapper;
import com.abby.jiaqing.response.ResponseWriter;
import com.abby.jiaqing.security.ResponseCode;
import com.abby.jiaqing.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
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
        response.setContentType("application/json;");
        response.setCharacterEncoding("UTF-8");
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

    @GetMapping(value = "/get")
    public void getUserData(HttpServletRequest request,HttpServletResponse response) throws IOException {
        User user=userService.getUserData();
        String responseStr;
        if(user==null){
           responseStr=ResponseWrapper.wrap(ResponseCode.USER_NOT_FOUND,"user not found");
        }else{
            Map<String,Object> map=ResponseWrapper.wrapNeedAdditionalOps(ResponseCode.SUCCESS,"got user data successfully");
            map.put("user",user);
            responseStr=ResponseWrapper.writeAsString(map);
        }
        ResponseWriter.writeToResponseThenClose(response,responseStr);
    }
}
