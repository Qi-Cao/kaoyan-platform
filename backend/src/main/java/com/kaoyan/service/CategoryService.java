package com.kaoyan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaoyan.entity.Category;
import com.kaoyan.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {
    
    public List<Category> getCategories() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort_order");
        return this.list(queryWrapper);
    }
    
    public List<Category> getForumCategories() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", 0).orderByAsc("sort_order");
        return this.list(queryWrapper);
    }
}
