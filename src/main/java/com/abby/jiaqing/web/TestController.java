package com.abby.jiaqing.web;

import javax.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Resource
    private WxMpService wxMpService;

    @RequestMapping(value ="/test")
    public String te() throws WxErrorException {
       return wxMpService.getMaterialService().materialNewsBatchGet(0,20).toString();
    }
}
