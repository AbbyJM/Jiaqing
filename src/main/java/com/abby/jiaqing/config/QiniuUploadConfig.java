package com.abby.jiaqing.config;

import com.qiniu.storage.BucketManager;
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
    public com.qiniu.storage.Configuration configuration(){
        return new com.qiniu.storage.Configuration(Region.region2());
    }

    @Bean
    public UploadManager uploadManager(){
        return new UploadManager(configuration());
    }

    @Bean
    public BucketManager bucketManager(){
        return new BucketManager(auth(),configuration());
    }
}
