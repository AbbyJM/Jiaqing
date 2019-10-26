package com.abby.jiaqing.web;

import com.abby.jiaqing.service.TokenService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/")
    public void validate(HttpServletRequest request, HttpServletResponse response){
        String echoStr=request.getParameter("echostr");
        if(tokenService.validate(request)){
            PrintWriter writer= null;
            try {
                writer = response.getWriter();
                writer.write(echoStr);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.write(echoStr);
        }
    }
}
