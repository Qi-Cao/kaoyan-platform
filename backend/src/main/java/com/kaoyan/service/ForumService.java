package com.kaoyan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaoyan.entity.Forum;
import com.kaoyan.mapper.ForumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForumService extends ServiceImpl<ForumMapper, Forum> {
    
    public Page<Forum> getForumPage(int pageNum, int pageSize, Long categoryId) {
        Page<Forum> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Forum> queryWrapper = new QueryWrapper<>();
        
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }
        
        queryWrapper.eq("status", 1).orderByDesc("created_at");
        return this.page(page, queryWrapper);
    }
    
    public Forum getForumDetail(Long id) {
        Forum forum = this.getById(id);
        if (forum != null) {
            // 增加浏览量
            forum.setViewCount(forum.getViewCount() + 1);
            this.updateById(forum);
        }
        return forum;
    }
}
