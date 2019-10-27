package com.abby.jiaqing.config;

import javax.annotation.Resource;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
public class WechatMPServiceConfig {
    @Resource
    private WechatProperties wechatProperties;

    @Bean
    public WxMpService wxMpService(){
        WxMpDefaultConfigImpl config=new WxMpDefaultConfigImpl();
        config.setAppId(wechatProperties.getAppId());
        config.setSecret(wechatProperties.getSecret());
        config.setToken(wechatProperties.getToken());
        config.setAesKey(wechatProperties.getAseKey());
        WxMpService wxMpService=new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }
}
