package com.abby.jiaqing.web;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
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
            WxMpXmlMessage message=getMessage(request,timestamp,nonce);
            WxMpXmlOutMessage outMessage=wxMpMessageRouter.route(message);
            response.getWriter().write(outMessage.toXml());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WxMpXmlMessage getMessage(HttpServletRequest request,String timestamp,String nonce) throws IOException {
        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ?
            "raw" :
            request.getParameter("encrypt_type");

        WxMpXmlMessage inMessage = null;
        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
        } else if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
        }
        return inMessage;
    }
}
