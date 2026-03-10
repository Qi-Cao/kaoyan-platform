package com.kaoyan.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaoyan.entity.Resource;
import com.kaoyan.entity.ResourceReview;
import com.kaoyan.mapper.ResourceReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceReviewService extends ServiceImpl<ResourceReviewMapper, ResourceReview> {
    
    @Autowired
    private ResourceService resourceService;
    
    public boolean reviewResource(Long resourceId, Long reviewerId, Integer status, String reason) {
        ResourceReview review = new ResourceReview();
        review.setResourceId(resourceId);
        review.setReviewerId(reviewerId);
        review.setStatus(status);
        review.setReason(reason);
        boolean saved = this.save(review);
        
        if (saved) {
            Resource resource = resourceService.getById(resourceId);
            if (resource != null) {
                resource.setStatus(status);
                resourceService.updateById(resource);
            }
        }
        return saved;
    }
}
