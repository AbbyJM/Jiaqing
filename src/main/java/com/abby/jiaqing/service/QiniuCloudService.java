package com.abby.jiaqing.service;

public interface QiniuCloudService {
    boolean uploadImage(String filePath,String fileName);
    String getImageURL(String fileName);
}
