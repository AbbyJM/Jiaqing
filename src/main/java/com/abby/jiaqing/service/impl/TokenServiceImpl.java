package com.abby.jiaqing.service.impl;

import com.abby.jiaqing.config.WechatProperties;
import com.abby.jiaqing.service.TokenService;
import com.abby.jiaqing.utils.SHAUtil;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private WechatProperties wechatProperties;
    public boolean validate(HttpServletRequest request) {
        String signature=request.getParameter("signature");
        String nonce=request.getParameter("nonce");
        String timestamp=request.getParameter("timestamp");
        String[] validation=new String[]{wechatProperties.getToken(),timestamp,nonce};
        Arrays.sort(validation);
        StringBuilder buffer = new StringBuilder();
        for (String aValidation : validation) {
            buffer.append(aValidation);
        }
        String hashCode= SHAUtil.encode(buffer.toString());
        return hashCode.equals(signature);
    }
}
