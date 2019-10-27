package com.abby.jiaqing.service.impl;

import com.abby.jiaqing.mapper.ImageMapper;
import com.abby.jiaqing.model.domain.Image;
import com.abby.jiaqing.service.ImageService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    private ImageMapper imageMapper;

    public String getImageMediaIdByName(String name) {
        List<Image> images=imageMapper.selectByName(name);
        if(images!=null&&!images.isEmpty()){
            return images.get(0).getMediaId();
        }
        return null;
    }
}
