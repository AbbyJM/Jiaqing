package com.abby.jiaqing.web;

import com.abby.jiaqing.mapper.ImageMapper;

import com.abby.jiaqing.model.domain.Image;
import com.abby.jiaqing.response.ResponseWrapper;
import com.abby.jiaqing.response.ResponseWriter;
import com.abby.jiaqing.security.ResponseCode;
import com.abby.jiaqing.service.QiniuCloudService;
import com.abby.jiaqing.utils.ImageUtil;
import com.abby.jiaqing.utils.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/image")
public class ImageController {
    private Logger logger= LoggerFactory.getLogger(ImageController.class);

    //微信的外链只能在腾讯系中使用
    //如果用本机来做资源服务器因为带宽太小体验很差，所以使用七牛云来同步存储照片
    @Resource
    private QiniuCloudService qiniuCloudService;

    @Resource
    private WxMpService wxMpService;

    @Resource
    private ImageMapper imageMapper;

    @Resource
    private ObjectMapper objectMapper;
    private String mediaId;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @GetMapping(value = "/download")
    public String downloadImage(HttpServletRequest request, HttpServletResponse response){
        HttpSession session=request.getSession();
        String s=session.getId();
        System.out.println("session"+s);
        return "test";
    }

    @GetMapping(value = "/list")
    public void getImageList(HttpServletRequest request,HttpServletResponse response){
        int pageNum=request.getParameter("pageNum")!=null?Integer.parseInt(request.getParameter("pageNum")):1;
        if(pageNum<=0){
            pageNum=1;
        }
        int pageCount=request.getParameter("pageSize") !=null?Integer.parseInt(request.getParameter("pageSize")):1;
        if(pageCount<=0){
            pageCount=1;
        }
        PageHelper.startPage(pageNum,pageCount);
        List<Image> images=imageMapper.getAllImages();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> map=ResponseWrapper.wrapNeedAdditionalOps(ResponseCode.SUCCESS,"get image list successfully");
        map.put("data",images);
        try {
            ResponseWriter.writeToResponseThenClose(response,objectMapper.writeValueAsString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            CountDownLatch Lock=new CountDownLatch(2);
            OpResult uploadResult=new OpResult();

            uploadToQiniu(fileName,Lock,uploadResult);
            uploadToWechat(f,fileName,Lock,uploadResult);
            try {
                Lock.await(20, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(uploadResult.getQiniu()&&uploadResult.getWechat()){
               Image imageObj=new Image();
               imageObj.setMediaId(mediaId);
               //文件名有.jpg或.png后缀等，去掉后缀保存
               int index=fileName.lastIndexOf(".");
               imageObj.setName(fileName.substring(0,index));
               imageObj.setUrl(qiniuCloudService.getImageURL(fileName));
               imageObj.setTime(TimeUtil.getCurrentTime());
               //插入记录到数据库
               if(imageMapper.insert(imageObj)>0){
                   Map<String,Object> ret=ResponseWrapper.wrapNeedAdditionalOps(ResponseCode.UPLOAD_IMAGE_SUCCESS,"uploaded image successfully");
                   ret.put("image",imageObj);
                   responseStr=objectMapper.writeValueAsString(ret);
                   logger.info("upload image "+fileName+" successfully");
               }else{
                   //插入数据库失败
                   responseStr=ResponseWrapper.wrap(ResponseCode.UPLOAD_RESULT_WRITE_FAILED,"failed to insert a record to database");
                   logger.error("failed to write record to database");
               }
            }else {
                if(!uploadResult.getQiniu()&&!uploadResult.getWechat()){
                    responseStr=ResponseWrapper.wrap(ResponseCode.UPLOAD_IMAGE_FAILED,"failed to upload image to qiniu cloud and wechat");
                    logger.error("failed to upload image to qiniu cloud and wechat");
                }else {
                    responseStr=ResponseWrapper.wrap(ResponseCode.UPLOAD_IMAGE_FAILED,!uploadResult.getWechat()?"" +
                        "failed to upload image to wechat":"failed to upload image to qiniu");
                    logger.error(!uploadResult.getWechat()?"" +
                        "failed to upload image to wechat":"failed to upload image to qiniu");
                }
            }
        }
        ResponseWriter.writeToResponseThenClose(response,responseStr);
    }


    @DeleteMapping(value = "/delete")
    public void deleteImage(HttpServletRequest request,HttpServletResponse response) throws IOException, InterruptedException {
        int imageId=request.getParameter("imageId")!=null?Integer.parseInt(request.getParameter("imageId")):-1;
        String responseStr="";
        CountDownLatch deleteLock=new CountDownLatch(2);
        OpResult deleteResult=new OpResult();
        if(imageId<0){
           responseStr=ResponseWrapper.wrap(ResponseCode.IMAGE_NOT_FOUND,"image not found");
        }else{
           Image image=imageMapper.selectByPrimaryKey(imageId);
           deleteFromQiniu(image.getName()+image.getUrl().substring(image.getUrl().lastIndexOf(".")),deleteLock,deleteResult);
           deleteFromWechat(image.getMediaId(),deleteLock,deleteResult);
           deleteLock.await(10,TimeUnit.SECONDS);
           if(deleteResult.getWechat()&&deleteResult.getQiniu()){
               if(imageMapper.deleteByPrimaryKey(imageId)>0) {
                   responseStr=ResponseWrapper.wrap(ResponseCode.SUCCESS,"deleted image successfully");
               }else{
                   responseStr=ResponseWrapper.wrap(ResponseCode.IMAGE_DELETE_FAILED,"failed to delete iamge from database");
               }
           }else {
               responseStr=ResponseWrapper.wrap(ResponseCode.IMAGE_DELETE_FAILED,"failed to delete iamge");
           }
        }
       ResponseWriter.writeToResponseThenClose(response,responseStr);
    }

    private void uploadToQiniu(String fileName,CountDownLatch uploadLock,OpResult result){
        taskExecutor.execute(new UploadToQiniuRunnable(uploadLock,result,fileName));
    }

    private void deleteFromQiniu(String fileName,CountDownLatch deleteLock,OpResult deleteResult){
        taskExecutor.execute(new DeleteFromQiniuRunnable(fileName,deleteLock,deleteResult));
    }

    private void deleteFromWechat(String mediaId,CountDownLatch deleteLock,OpResult deleteResult){
        taskExecutor.execute(new DeleteFromWechatRunnable(mediaId,deleteLock,deleteResult));
    }

    private void uploadToWechat(File f,String fileName,CountDownLatch uploadLock,OpResult uploadResult){
        taskExecutor.execute(new UploadToWechatRunnable(f,fileName,uploadResult,uploadLock));
    }

    static class OpResult{
        public  boolean qiniuDone=false;
        public  boolean wechatDone=false;
        public OpResult(){

        }
        public void setQiniuDone(boolean done){
            qiniuDone=done;
        }
        public boolean getQiniu(){
            return qiniuDone;
        }
        public void setWechatDone(boolean done){
            wechatDone=done;
        }
        public boolean getWechat(){
            return wechatDone;
        }
    }

     class UploadToQiniuRunnable implements Runnable{
        private CountDownLatch lock;
        private OpResult result;
        private String fileName;
        public UploadToQiniuRunnable(CountDownLatch lock,OpResult result,String fileName){
            this.lock=lock;
            this.result=result;
            this.fileName=fileName;
        }
        public void run() {
            logger.info("uploaing to qiniu in thread "+Thread.currentThread().getName());
            boolean success=qiniuCloudService.uploadImage(ImageUtil.getImageFilePath()+File.separator+fileName,fileName);
            if(success){
                logger.info("uploaded image "+fileName+" successfully");
                result.setQiniuDone(true);
            }
            lock.countDown();
        }
    }

    class UploadToWechatRunnable implements Runnable{
        private File f;
        private String fileName;
        private OpResult uploadResult;
        private CountDownLatch lock;
        public UploadToWechatRunnable(File f,String name,OpResult result,CountDownLatch lock){
            this.f=f;
            fileName=name;
            uploadResult=result;
            this.lock=lock;
        }
        public void run() {
            logger.info("uoloading image to wechat in thread "+Thread.currentThread().getName());
            WxMpMaterial wxMpMaterial=new WxMpMaterial();
            wxMpMaterial.setFile(f);
            wxMpMaterial.setName(fileName);
            try {
                WxMpMaterialUploadResult result=wxMpService.getMaterialService()
                    .materialFileUpload(WxConsts.MaterialType.IMAGE,wxMpMaterial);
                mediaId=result.getMediaId();
                logger.info("get url "+result.getUrl());
                if(result.getErrCode()==null&&result.getErrMsg()==null){
                    uploadResult.setWechatDone(true);
                }
            } catch (WxErrorException e) {
                e.printStackTrace();
            } finally {
                lock.countDown();
            }
        }
    }

    class DeleteFromQiniuRunnable implements Runnable{
        private String fileName;
        private CountDownLatch lock;
        private OpResult result;
        public DeleteFromQiniuRunnable(String fileName,CountDownLatch lock,OpResult result){
            this.fileName=fileName;
            this.lock=lock;
            this.result=result;
        }
        public void run() {
            if(qiniuCloudService.deleteImage(fileName)){
               result.setQiniuDone(true);
            }
            lock.countDown();
        }
    }

    class DeleteFromWechatRunnable implements Runnable{
        private String mediaId;
        private CountDownLatch lock;
        private OpResult result;
        public DeleteFromWechatRunnable(String mediaId,CountDownLatch lock,OpResult result){
            this.mediaId=mediaId;
            this.lock=lock;
            this.result=result;
        }
        public void run() {
            try {
                if(wxMpService.getMaterialService()
                    .materialDelete(mediaId)){
                    result.setWechatDone(true);
                }
            } catch (WxErrorException e) {
                e.printStackTrace();
            }finally {
                lock.countDown();
            }
        }
    }
}
