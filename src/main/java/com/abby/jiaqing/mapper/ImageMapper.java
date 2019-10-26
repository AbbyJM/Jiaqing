package com.abby.jiaqing.mapper;

import com.abby.jiaqing.model.domain.Image;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ImageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Image record);

    int insertSelective(Image record);

    Image selectByPrimaryKey(Integer id);

    List<Image> getAllImages();

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKey(Image record);
}