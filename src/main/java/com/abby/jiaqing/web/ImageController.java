package com.abby.jiaqing.web;

import com.abby.jiaqing.mapper.ImageMapper;

import com.abby.jiaqing.model.domain.Image;
import com.abby.jiaqing.response.ResponseWrapper;
import com.abby.jiaqing.response.ResponseWriter;
import com.abby.jiaqing.security.ResponseCode;
import com.abby.jiaqing.utils.ImageUtil;
import com.abby.jiaqing.utils.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/image")
public class ImageController {
    private Logger logger= LoggerFactory.getLogger(ImageController.class);

    @Resource
    private WxMpService wxMpService;

    @Resource
    private ImageMapper imageMapper;

    @Resource
    private ObjectMapper objectMapper;

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
    public void uploadImage(HttpServletRequest request,HttpServletResponse response) throws IOException, WxErrorException {
        String image = request.getParameter("image");
        String fileName = request.getParameter("fileName");
        String responseStr = "";
        int statusCode;
        if (image == null || fileName == null) {
            statusCode = fileName == null ? ResponseCode.IMAGE_NAME_NULL : ResponseCode.IMAGE_NULL;
            responseStr = ResponseWrapper.wrap(statusCode, "image or image name cannot be null");
        } else {
            List<Image> images=imageMapper.selectByName(fileName.substring(0,fileName.lastIndexOf(".")));
            if(images!=null&&!images.isEmpty()){
               responseStr=ResponseWrapper.wrap(ResponseCode.DUPLICATE_IMAGE,"duplicate image");
               ResponseWriter.writeToResponseThenClose(response,responseStr);
               return;
            }
            File f = ImageUtil.base64ToFile(image, fileName);
            if (f == null) {
                return;
            }
            WxMpMaterial wxMpMaterial=new WxMpMaterial();
            wxMpMaterial.setFile(f);
            wxMpMaterial.setName(fileName);
            WxMpMaterialUploadResult result=wxMpService.getMaterialService().materialFileUpload(WxConsts.MaterialType.IMAGE,wxMpMaterial);
            if(result.getErrCode()==null&&result.getErrMsg()==null){
                Image imageObj=new Image();
                imageObj.setMediaId(result.getMediaId());
                imageObj.setUrl(result.getUrl());
                imageObj.setTime(TimeUtil.getCurrentTime());
                int index=fileName.lastIndexOf(".");
                imageObj.setName(fileName.substring(0,index));
                if(imageMapper.insert(imageObj)>0) {
                    responseStr = ResponseWrapper.wrap(ResponseCode.UPLOAD_IMAGE_SUCCESS, "uploaded success");
                }else{
                    responseStr=ResponseWrapper.wrap(ResponseCode.UPLOAD_IMAGE_FAILED,"upload failed");
                    logger.info("failed to insert a record to database");
                }
            }else{
                responseStr=ResponseWrapper.wrap(ResponseCode.UPLOAD_IMAGE_FAILED,"upload failed");
            }
        }
        ResponseWriter.writeToResponseThenClose(response,responseStr);
    }


    @DeleteMapping(value = "/delete")
    public void deleteImage(HttpServletRequest request,HttpServletResponse response) throws IOException, InterruptedException, WxErrorException {
        int imageId=request.getParameter("imageId")!=null?Integer.parseInt(request.getParameter("imageId")):-1;
        String responseStr="";
        if(imageId<0){
           responseStr=ResponseWrapper.wrap(ResponseCode.IMAGE_NOT_FOUND,"image not found");
        }else{
           Image image=imageMapper.selectByPrimaryKey(imageId);
           if(image==null){
               responseStr=ResponseWrapper.wrap(ResponseCode.IMAGE_NOT_FOUND,"image not found");
               ResponseWriter.writeToResponseThenClose(response,responseStr);
               return;
           }
           if(wxMpService.getMaterialService().materialDelete(image.getMediaId())){
               if(imageMapper.deleteByPrimaryKey(image.getId())>0){
                   responseStr=ResponseWrapper.wrap(ResponseCode.SUCCESS,"delete success");
               }else{
                   logger.info("failed to insert a record to database");
                   responseStr=ResponseWrapper.wrap(ResponseCode.IMAGE_DELETE_FAILED,"delete failed");
               }
           }else{
               responseStr=ResponseWrapper.wrap(ResponseCode.IMAGE_DELETE_FAILED,"delete failed");
           }
        }
       ResponseWriter.writeToResponseThenClose(response,responseStr);
    }
}
