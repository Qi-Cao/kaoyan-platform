package com.kaoyan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("notices")
public class Notice {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    private String content;
    private Long userId;
    private Integer isTop;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
