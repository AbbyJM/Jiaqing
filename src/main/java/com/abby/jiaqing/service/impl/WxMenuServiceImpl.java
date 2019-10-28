package com.abby.jiaqing.service.impl;

import com.abby.jiaqing.service.MenuService;
import javax.annotation.Resource;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
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
        WxMenuButton button1=new WxMenuButton();
        button1.setName("嘉庆项目");
        button1.setKey("JIAQING");
        button1.setType(WxConsts.MenuButtonType.CLICK);

        WxMenuButton button11=new WxMenuButton();
        button11.setName("嘉庆摄影");
        button11.setType(WxConsts.MenuButtonType.CLICK);
        button11.setKey("PHOTO");
        WxMenuButton button12=new WxMenuButton();
        button12.setName("嘉庆驾校");
        button12.setType(WxConsts.MenuButtonType.CLICK);
        button12.setKey("DRIVE");
        WxMenuButton button13=new WxMenuButton();
        button13.setKey("CONTENT");
        button13.setName("业务内容");
        button13.setType(WxConsts.MenuButtonType.CLICK);
        button1.getSubButtons().add(button11);
        button1.getSubButtons().add(button12);
        button1.getSubButtons().add(button13);


        WxMenuButton button2=new WxMenuButton();
        button2.setName("毕业约拍照");
        button2.setKey("TAKE_PHOTO");
        button2.setType(WxConsts.MenuButtonType.CLICK);
        WxMenuButton button3=new WxMenuButton();
        button3.setName("联系方式");
        button3.setKey("CONTACT");
        button3.setType(WxConsts.MenuButtonType.CLICK);
        menu.getButtons().add(button1);
        menu.getButtons().add(button2);
        menu.getButtons().add(button3);
        return menu;
    }

    public WxMpMenu getMenu() throws WxErrorException {
       return wxMpService.getMenuService().menuGet();
    }
}
