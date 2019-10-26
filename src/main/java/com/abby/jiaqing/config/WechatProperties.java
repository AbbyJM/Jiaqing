package com.abby.jiaqing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource(value = "classpath:wechat-dev.yml",factory = YamlPropertyLoaderFactory.class)
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {
    private String token;
    private String appId;
    private String secret;
    private String aseKey;
    public void setToken(String token){
        this.token=token;
    }
    public String getToken(){
        return token;
    }

    public void setAppId(String id){
        appId=id;
    }

    public String getAppId(){
        return appId;
    }

    public void setSecret(String secret){
        this.secret=secret;
    }

    public String getSecret(){
        return secret;
    }

    public void setAseKey(String key){
        aseKey=key;
    }

    public String getAseKey(){
        return aseKey;
    }
}
