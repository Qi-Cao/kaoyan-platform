package com.kaoyan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("forum_replies")
public class ForumReply {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long forumId;
    private Long userId;
    private Long parentId;
    private String content;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
