package com.kaoyan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaoyan.entity.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchoolMapper extends BaseMapper<School> {}

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {}

@Mapper
public interface ForumMapper extends BaseMapper<Forum> {}

@Mapper
public interface ForumReplyMapper extends BaseMapper<ForumReply> {}

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {}

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {}

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {}
