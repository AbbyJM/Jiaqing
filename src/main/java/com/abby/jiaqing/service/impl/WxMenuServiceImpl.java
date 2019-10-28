package com.abby.jiaqing.service.impl;

import com.abby.jiaqing.service.MenuService;
import javax.annotation.Resource;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.springframework.stereotype.Service;

@Service
public class WxMenuServiceImpl implements MenuService {
   @Resource
   private WxMpService wxMpService;

    public WxMenu createMenu() {
        WxMenu menu=new WxMenu();
        return null;
    }

    public WxMpMenu getMenu() throws WxErrorException {
       return wxMpService.getMenuService().menuGet();
    }
}
