package com.kaoyan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.math.BigDecimal;

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
    private LocalDateTime createdAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAdmissionRate() { return admissionRate; }
    public void setAdmissionRate(BigDecimal admissionRate) { this.admissionRate = admissionRate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
