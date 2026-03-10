package com.kaoyan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.Result;
import com.kaoyan.entity.ResourceComment;
import com.kaoyan.entity.User;
import com.kaoyan.service.ResourceCommentService;
import com.kaoyan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resource-comments")
@CrossOrigin
public class ResourceCommentController {
    
    @Autowired
    private ResourceCommentService resourceCommentService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public Result<Page<ResourceComment>> getComments(
            @RequestParam Long resourceId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<ResourceComment> page = resourceCommentService.getCommentPage(resourceId, pageNum, pageSize);
        return Result.success(page);
    }
    
    @PostMapping
    public Result<ResourceComment> addComment(Authentication authentication, @RequestBody ResourceComment comment) {
        User user = userService.getUserByUsername(authentication.getName());
        comment.setUserId(user.getId());
        resourceCommentService.save(comment);
        return Result.success(comment);
    }
}
