package com.abby.jiaqing.wechat;

import java.util.Map;
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

    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
        WxSessionManager sessionManager) throws WxErrorException {
        String content=wxMessage.getContent();
        logger.info(content);
        return null;
    }
}
