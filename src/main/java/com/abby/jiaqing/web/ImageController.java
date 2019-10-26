package com.abby.jiaqing.web;

import com.abby.jiaqing.mapper.ImageMapper;

import com.abby.jiaqing.model.domain.Image;
import com.abby.jiaqing.response.ResponseWrapper;
import com.abby.jiaqing.security.ResponseCode;
import com.abby.jiaqing.service.QiniuCloudService;
import com.abby.jiaqing.utils.ImageUtil;
import com.abby.jiaqing.utils.TimeUtil;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/image")
public class ImageController {
    Logger logger= LoggerFactory.getLogger(ImageController.class);

    @Resource
    private QiniuCloudService qiniuCloudService;

    @Resource
    private WxMpService wxMpService;

    @Resource
    private ImageMapper imageMapper;

    private CountDownLatch uploadLock;

    private boolean uploadToQiniuSuccess;

    private boolean uploadToWechatSuccess;

    private String mediaId;

    @GetMapping(value = "/download")
    public String downloadImage(HttpServletRequest request, HttpServletResponse response){
        HttpSession session=request.getSession();
        String s=session.getId();
        System.out.println("session"+s);
        return "test";
    }

    @PostMapping(value = "/upload")
    public void uploadImage(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String image=request.getParameter("image");
        String fileName=request.getParameter("fileName");
        String responseStr="";
        int statusCode;
        if(image==null||fileName==null){
           statusCode= fileName==null?ResponseCode.IMAGE_NAME_NULL:ResponseCode.IMAGE_NULL;
           responseStr= ResponseWrapper.wrap(statusCode,"image or image name cannot be null");
        }else {
            File f= ImageUtil.base64ToFile(image,fileName);
            if(f==null){
                return;
            }
            //需要同时上传到七牛云以及微信公众号，初始化状态
            uploadLock=new CountDownLatch(2);
            uploadToQiniuSuccess=false;
            uploadToWechatSuccess=false;

            uploadToQiniu(fileName);
            uploadToWechat(f,fileName);
            try {
                uploadLock.await(20, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(uploadToWechatSuccess&&uploadToQiniuSuccess){
               Image imageObj=new Image();
               imageObj.setMediaId(mediaId);
               imageObj.setName(fileName);
               imageObj.setUrl(qiniuCloudService.getImageURL(fileName));
               imageObj.setTime(TimeUtil.getCurrentTime());
               //插入记录到数据库
               if(imageMapper.insert(imageObj)>0){
                   responseStr=ResponseWrapper.wrap(ResponseCode.UPLOAD_IMAGE_SUCCESS,"uploaded image successfully");
                   logger.info("upload image "+fileName+" successfully");
               }else{
                   //插入数据库失败
                   responseStr=ResponseWrapper.wrap(ResponseCode.UPLOAD_RESULT_WRITE_FAILED,"failed to insert a record to database");
                   logger.error("failed to write record to database");
               }
            }else {
                if(!uploadToQiniuSuccess&&!uploadToWechatSuccess){
                    responseStr=ResponseWrapper.wrap(ResponseCode.UPLOAD_IMAGE_FAILED,"failed to upload image to qiniu cloud and wechat");
                    logger.error("failed to upload image to qiniu cloud and wechat");
                }else {
                    responseStr=ResponseWrapper.wrap(ResponseCode.UPLOAD_IMAGE_FAILED,!uploadToWechatSuccess?"" +
                        "failed to upload image to wechat":"failed to upload image to qiniu");
                    logger.error(!uploadToWechatSuccess?"" +
                        "failed to upload image to wechat":"failed to upload image to qiniu");
                }
            }
        }
        response.setContentType("application/json;utf-8");
        PrintWriter writer=response.getWriter();
        writer.write(responseStr);
        writer.flush();
        writer.close();
    }

    @Async(value ="taskExecutor")
    void uploadToQiniu(String fileName){
        //把文件上传至七牛云空间
        boolean success=qiniuCloudService.uploadImage(ImageUtil.getImageFilePath()+File.separator+fileName,fileName);
        if(success){
            logger.info("uploaded image "+fileName+" successfully");
            uploadToQiniuSuccess=true;
        }
        uploadLock.countDown();
    }

    @Async(value = "taskExecutor")
    void uploadToWechat(File f,String fileName){
        WxMpMaterial wxMpMaterial=new WxMpMaterial();
        wxMpMaterial.setFile(f);
        wxMpMaterial.setName(fileName);
        try {
            WxMpMaterialUploadResult result=wxMpService.getMaterialService()
                .materialFileUpload(WxConsts.MaterialType.IMAGE,wxMpMaterial);
            mediaId=result.getMediaId();
            logger.info("error message "+result.getErrMsg());
            logger.info("error code "+result.getErrCode());
            if(mediaId==null&&result.getUrl()!=null){
               uploadToWechatSuccess=true;
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        } finally {
            uploadLock.countDown();
        }
    }
}
