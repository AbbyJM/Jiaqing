package com.abby.jiaqing.web;

import javax.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Resource
    private WxMpService wxMpService;
    Logger logger= LoggerFactory.getLogger(TestController.class);
    @GetMapping(value = "/test")
    public String test() throws WxErrorException {
        WxMpMaterialNewsBatchGetResult result=wxMpService.getMaterialService().materialNewsBatchGet(0,20);
        logger.info(result.toString());
        return result.toString();
    }

    @RequestMapping(value = "/needLogin")
    public String login(){
        System.out.println("needlogin");
        return "needLogin";
    }
}
