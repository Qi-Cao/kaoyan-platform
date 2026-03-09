package com.kaoyan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resources")
public class Resource {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    private String category;
    private String content;
    private String fileUrl;
    private Long userId;
    private Integer viewCount;
    private Integer downloadCount;
    private Integer status; // 0-待审核 1-已发布
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
