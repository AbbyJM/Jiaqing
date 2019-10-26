package com.abby.jiaqing.web;

import com.abby.jiaqing.response.ResponseWrapper;
import com.abby.jiaqing.security.ResponseCode;
import com.abby.jiaqing.service.QiniuCloudService;
import com.abby.jiaqing.utils.ImageUtil;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
        System.out.println(image);
        System.out.println(fileName);
        String responseStr="";
        int statusCode;
        if(image==null||fileName==null){
           statusCode= fileName==null?ResponseCode.IMAGE_NAME_NULL:ResponseCode.IMAGE_NULL;
           responseStr= ResponseWrapper.wrap(statusCode,"image or image name cannot be null");
        }else {
            File f= ImageUtil.base64ToFile(image,fileName);
            if(f==null){
                statusCode=ResponseCode.IMAGE_DAMAGED;
                responseStr=ResponseWrapper.wrap(statusCode,"file is not image or destroyed");
            }
            //把文件上传至七牛云空间
            boolean success=qiniuCloudService.uploadImage(ImageUtil.getImageFilePath()+File.separator+fileName,fileName);
            if(success){
                logger.info("uploaded image "+fileName+" successfully");
                System.out.println(qiniuCloudService.getImageURL(fileName));
            }

            WxMpMaterial wxMpMaterial=new WxMpMaterial();
            wxMpMaterial.setFile(f);
            wxMpMaterial.setName(fileName);
            try {
                WxMpMaterialUploadResult result=wxMpService.getMaterialService()
                    .materialFileUpload(WxConsts.MaterialType.IMAGE,wxMpMaterial);
                logger.info(result.toString());
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
        response.setContentType("application/json;utf-8");
        PrintWriter writer=response.getWriter();
        writer.write(responseStr);
        writer.flush();
        writer.close();
    }
}
