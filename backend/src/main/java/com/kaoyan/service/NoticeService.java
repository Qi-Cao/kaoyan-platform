package com.kaoyan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaoyan.entity.Notice;
import com.kaoyan.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {
    
    public List<Notice> getNotices() {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_top", 1).orderByDesc("created_at");
        List<Notice> topNotices = this.list(queryWrapper);
        
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_top", 0).orderByDesc("created_at").last("LIMIT 10");
        List<Notice> normalNotices = this.list(queryWrapper);
        
        topNotices.addAll(normalNotices);
        return topNotices;
    }
    
    public Page<Notice> getNoticePage(int pageNum, int pageSize) {
        Page<Notice> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("is_top").orderByDesc("created_at");
        return this.page(page, queryWrapper);
    }
}
