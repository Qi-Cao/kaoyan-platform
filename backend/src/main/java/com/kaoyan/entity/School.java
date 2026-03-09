package com.kaoyan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("schools")
public class School {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    private String province;
    private String city;
    private Long categoryId;
    private Integer rank;
    private String logo;
    private String description;
    private BigDecimal admissionRate;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
