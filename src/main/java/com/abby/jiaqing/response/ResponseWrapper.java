package com.abby.jiaqing.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResponseWrapper {
    private static ObjectMapper objectMapper;

    @Resource
    public void setObjectMapper(ObjectMapper mapper){
        objectMapper=mapper;
    }

    public static String wrap(int code,String message) throws JsonProcessingException {
        Map<String,Object> result=new HashMap<String, Object>(2);
        result.put("status",code);
        result.put("message",message);
        return objectMapper.writeValueAsString(result);
    }
}
