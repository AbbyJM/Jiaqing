package com.abby.jiaqing.service;

import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;

public interface MenuService {
    WxMenu createMenu();
    WxMpMenu getMenu() throws WxErrorException;
}
