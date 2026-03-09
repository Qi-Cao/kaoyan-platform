package com.kaoyan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("forum_replies")
public class ForumReply {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long forumId;
    private Long userId;
    private Long parentId;
    private String content;
    private LocalDateTime createdAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getForumId() { return forumId; }
    public void setForumId(Long forumId) { this.forumId = forumId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
