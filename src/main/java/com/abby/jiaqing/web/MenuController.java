package com.abby.jiaqing.web;

import com.abby.jiaqing.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private WxMpService wxMpService;

    @GetMapping(value = "/get")
    public void getMenu(HttpServletRequest request, HttpServletResponse response) throws WxErrorException, IOException {
        WxMpMenu wxMpMenu=menuService.getMenu();
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(wxMpMenu.toJson());
    }

    @PostMapping(value = "/new")
    public void createNewMenu(HttpServletRequest request,HttpServletResponse response){
        WxMenu menu=menuService.createMenu();
        try {
            wxMpService.getMenuService().menuCreate(menu);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
