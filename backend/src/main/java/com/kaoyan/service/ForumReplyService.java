package com.kaoyan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaoyan.entity.Forum;
import com.kaoyan.entity.ForumReply;
import com.kaoyan.mapper.ForumReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForumReplyService extends ServiceImpl<ForumReplyMapper, ForumReply> {
    
    @Autowired
    private ForumService forumService;
    
    public Page<ForumReply> getReplyPage(Long forumId, int pageNum, int pageSize) {
        Page<ForumReply> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ForumReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("forum_id", forumId).orderByAsc("created_at");
        return this.page(page, queryWrapper);
    }
    
    public boolean addReply(ForumReply reply) {
        boolean saved = this.save(reply);
        if (saved) {
            Forum forum = forumService.getById(reply.getForumId());
            if (forum != null) {
                forum.setReplyCount(forum.getReplyCount() + 1);
                forumService.updateById(forum);
            }
        }
        return saved;
    }
}
