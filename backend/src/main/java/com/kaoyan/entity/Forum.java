package com.kaoyan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("forums")
public class Forum {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    private String content;
    private Long categoryId;
    private Long userId;
    private Integer viewCount;
    private Integer replyCount;
    private Integer status; // 0-隐藏 1-正常
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
