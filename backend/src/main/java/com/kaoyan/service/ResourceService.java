package com.kaoyan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaoyan.entity.Resource;
import com.kaoyan.mapper.ResourceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceService extends ServiceImpl<ResourceMapper, Resource> {
    
    public Page<Resource> getResourcePage(int pageNum, int pageSize, String category) {
        Page<Resource> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        
        if (category != null && !category.isEmpty()) {
            queryWrapper.eq("category", category);
        }
        
        queryWrapper.eq("status", 1).orderByDesc("created_at");
        return this.page(page, queryWrapper);
    }
    
    public Resource getResourceDetail(Long id) {
        Resource resource = this.getById(id);
        if (resource != null) {
            resource.setViewCount(resource.getViewCount() + 1);
            this.updateById(resource);
        }
        return resource;
    }
    
    public boolean addResource(Resource resource) {
        resource.setViewCount(0);
        resource.setDownloadCount(0);
        resource.setStatus(0); // 待审核
        return this.save(resource);
    }
}
