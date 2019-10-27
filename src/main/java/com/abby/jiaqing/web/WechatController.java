package com.abby.jiaqing.web;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WechatController {
    private static Logger logger= LoggerFactory.getLogger(WechatController.class);
    @Resource
    private WxMpService wxMpService;

    @Resource
    private WxMpMessageRouter wxMpMessageRouter;

    @RequestMapping(value = "/wechat")
    public void wechatPorcess(HttpServletRequest request,HttpServletResponse response){
        String signature=request.getParameter("signature");
        String nonce=request.getParameter("nonce");
        String timestamp=request.getParameter("timestamp");
        if(!wxMpService.checkSignature(timestamp,nonce,signature)){
            //不是微信发来的请求，do nothing
            logger.info("got a request that does not belong to wechat");
           return;
        }
        try {
            WxMpXmlMessage message=WxMpXmlMessage.fromXml(request.getInputStream());
            logger.info("got message "+message.toString());
            WxMpXmlOutMessage outMessage=wxMpMessageRouter.route(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
