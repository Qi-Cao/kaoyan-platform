package com.kaoyan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_profiles")
public class UserProfile {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private String realName;
    private String avatar;
    private Integer gender;
    private String school;
    private String major;
    private String bio;
}
