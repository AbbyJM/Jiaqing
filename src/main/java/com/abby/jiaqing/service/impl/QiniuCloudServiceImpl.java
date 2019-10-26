package com.abby.jiaqing.service.impl;

import com.abby.jiaqing.config.QiniuProperties;
import com.abby.jiaqing.service.QiniuCloudService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class QiniuCloudServiceImpl implements QiniuCloudService {
    @Resource
    private QiniuProperties qiniuProperties;

    @Resource
    private Auth auth;

    @Resource
    private UploadManager uploadManager;

    public boolean uploadImage(String filePath,String fileName){
        String uploadToken=auth.uploadToken(qiniuProperties.getDefaultBucket(),fileName);
        try {
            Response response=uploadManager.put(filePath,fileName,uploadToken);
            return response.error==null;
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getImageURL(String fileName)  {
        return String.format("%s/%s",qiniuProperties.getDomain(),fileName);
    }
}
