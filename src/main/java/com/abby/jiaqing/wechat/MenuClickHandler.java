package com.abby.jiaqing.wechat;

import com.abby.jiaqing.mapper.ImageMapper;
import com.abby.jiaqing.model.domain.Image;
import java.util.Map;
import javax.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MenuClickHandler implements WxMpMessageHandler {

    @Resource
    private ImageMapper imageMapper;

    Logger logger= LoggerFactory.getLogger(MenuClickHandler.class);

    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
        WxSessionManager sessionManager) throws WxErrorException {
        logger.info("wxmessage is "+wxMessage);
        logger.info("event key is "+wxMessage.getEvent());
        String key=wxMessage.getEventKey();
        WxMpXmlOutMessage outMessage=null;
        if(key.equals("CONTENT")){
           outMessage=WxMpXmlOutMessage.TEXT().content("业务内容：\n" +
               "项目1：精修+拍摄三组表情+电子版+纸质版+服装：38元\n" +
               "项目2：化妆简约造型+精修+拍摄三组表情+电子版+纸质版+服装：78元\n" +
               "项目3：每加印一版照片（1/2/3/5/6/7/）寸照片：10元\n" +
               "项目4：商务形象照：198元\n" +
               "项目5：西服租赁：30/套/天。\n" +
               "项目6：外拍写真视要求而定，可提供服装。")
               .fromUser(wxMessage.getToUser())
               .toUser(wxMessage.getFromUser())
               .build();
        }else if(key.equals("PHOTO")){
            Image image=imageMapper.selectByName("嘉庆摄影").get(0);
            outMessage=WxMpXmlOutMessage.IMAGE()
                .mediaId(image.getMediaId())
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .build();
        }else if(key.equals("DRIVE")){
            Image image=imageMapper.selectByName("嘉庆驾校").get(0);
            outMessage=WxMpXmlOutMessage.IMAGE()
                .mediaId(image.getMediaId())
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .build();
        }else if(key.equals("CONTACT")){
            outMessage=WxMpXmlOutMessage.TEXT()
                .content("唯一指定地点：天津城建大学众创空间内\n" +
                    "联系方式：18822037745（同微信）")
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .build();
        }else if(key.equals("TAKE_PHOTO")){
            WxMpXmlOutNewsMessage.Item item=new WxMpXmlOutNewsMessage.Item();
            item.setUrl("http://mp.weixin.qq.com/s?__biz=Mzg2NDA1MzM1Mg==&mid=100000848&idx=1&sn=1b7b630b4134b4392cd33126ed4f6ede&chksm=4e6e75577919fc41b49c9fdcbd951544806e11f3e5982fd6c2b78ccfc357d3e0c31b6b5a52f6#rd");
            item.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/Z5e3Ru2dQ1dop3Zht1Uiaa1xTSibZuuibmsDic6E1w71fzITgZ38A81gbm53icTEWUV9qibsITUePpvEBK8ibOmS1uNEw/0?wx_fmt=jpeg");
            item.setTitle("嘉庆摄影工作室丨毕业季约拍");
            item.setDescription("那年夏天不期而遇，今年夏天拿什么留住你？");
            outMessage=WxMpXmlOutMessage.NEWS()
                .addArticle(item)
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .build();
        }
        logger.info("out message is "+outMessage);
        return outMessage;
    }
}
