package com.abby.jiaqing.wechat;

import com.abby.jiaqing.service.ImageService;
import java.util.Map;
import javax.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReplyImageHandler implements WxMpMessageHandler {
    private static Logger logger= LoggerFactory.getLogger(ReplyImageHandler.class);

    @Resource
    private ImageService imageService;

    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
        WxSessionManager sessionManager) throws WxErrorException {
        WxMpXmlOutMessage outMessage;
        String fileName=wxMessage.getContent();
        String mediaId=imageService.getImageMediaIdByName(fileName);
        if(mediaId==null){
            outMessage=WxMpXmlOutMessage.TEXT()
                .content("没有找到您想要的内容哦")
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .build();
        }else {
            logger.info("got media for user "+wxMessage.getFromUser());
            outMessage = WxMpXmlOutMessage.IMAGE()
                .mediaId(mediaId)
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .build();
        }
        logger.info("out message in image handler"+outMessage.toString());
        return outMessage;
    }
}
