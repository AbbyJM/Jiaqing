package com.abby.jiaqing.config;

import com.abby.jiaqing.wechat.MenuClickHandler;
import com.abby.jiaqing.wechat.ReplyImageHandler;
import javax.annotation.Resource;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxMessageRouterConfig {
    @Resource
    private WxMpService wxMpService;

    @Resource
    private ReplyImageHandler replyImageHandler;

    @Resource
    private MenuClickHandler menuClickHandler;

    @Bean
    public WxMpMessageRouter router(){
        WxMpMessageRouter router=new WxMpMessageRouter(wxMpService);
        router.rule()
            .async(false)
            .msgType(WxConsts.XmlMsgType.TEXT)
            .handler(replyImageHandler)
            .end()
            .rule()
            .async(false)
            .msgType(WxConsts.MenuButtonType.CLICK)
            .handler(menuClickHandler)
            .end();
        return router;
    }
}
