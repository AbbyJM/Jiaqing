package com.abby.jiaqing.web;

import com.abby.jiaqing.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.chanjar.weixin.common.error.WxErrorException;

import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    @Resource
    private ObjectMapper objectMapper;

    @GetMapping(value = "/get")
    public void getMenu(HttpServletRequest request, HttpServletResponse response) throws WxErrorException, IOException {
        WxMpMenu wxMpMenu=menuService.getMenu();
        response.setContentType("application/json;utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(wxMpMenu));
    }
}
