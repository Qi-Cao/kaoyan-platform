package com.kaoyan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaoyan.entity.Forum;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ForumMapper extends BaseMapper<Forum> {
}
