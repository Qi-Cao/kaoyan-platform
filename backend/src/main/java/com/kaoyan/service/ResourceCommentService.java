package com.kaoyan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaoyan.entity.ResourceComment;
import com.kaoyan.mapper.ResourceCommentMapper;
import org.springframework.stereotype.Service;

@Service
public class ResourceCommentService extends ServiceImpl<ResourceCommentMapper, ResourceComment> {
    
    public Page<ResourceComment> getCommentPage(Long resourceId, int pageNum, int pageSize) {
        Page<ResourceComment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ResourceComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resource_id", resourceId).orderByDesc("created_at");
        return this.page(page, queryWrapper);
    }
    
    public Page<ResourceComment> getAllCommentPage(int pageNum, int pageSize, Long resourceId) {
        Page<ResourceComment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ResourceComment> queryWrapper = new QueryWrapper<>();
        if (resourceId != null) {
            queryWrapper.eq("resource_id", resourceId);
        }
        queryWrapper.orderByDesc("created_at");
        return this.page(page, queryWrapper);
    }
}
