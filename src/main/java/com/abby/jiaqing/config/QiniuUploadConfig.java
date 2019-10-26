package com.abby.jiaqing.config;

import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QiniuUploadConfig {
    @Resource
    private QiniuProperties qiniuProperties;

    @Bean
    public Auth auth(){
        return Auth.create(qiniuProperties.getAccessKey(),qiniuProperties.getSecretKey());
    }

    @Bean
    public UploadManager uploadManager(){
        com.qiniu.storage.Configuration configuration=new com.qiniu.storage.Configuration(Region.region2());
        return new UploadManager(configuration);
    }
}
