package com.abby.jiaqing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource(value = "classpath:qiniu-dev.yml",factory = YamlPropertyLoaderFactory.class)
@Component
@ConfigurationProperties(prefix = "qiniu")
public class QiniuProperties {
    private String accessKey;
    private String secretKey;
    private String defaultBucket;
    private int expire;
    private String domain;


    public void setDomain(String domain){
        this.domain=domain;
    }

    public String getDomain(){
        return domain;
    }

    public void setAccessKey(String key){
        accessKey=key;
    }

    public String getAccessKey(){
        return accessKey;
    }

    public void setSecretKey(String key){
        secretKey=key;
    }

    public String getSecretKey(){
        return secretKey;
    }

    public void setDefaultBucket(String bucket){
        defaultBucket=bucket;
    }

    public String getDefaultBucket(){
        return defaultBucket;
    }

    public void setExpire(int expire){
        this.expire=expire;
    }

    public int getExpire(){
        return expire;
    }
}
