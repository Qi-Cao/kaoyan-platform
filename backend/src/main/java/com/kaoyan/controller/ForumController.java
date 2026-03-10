package com.kaoyan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.Result;
import com.kaoyan.entity.Forum;
import com.kaoyan.entity.User;
import com.kaoyan.service.ForumService;
import com.kaoyan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forums")
@RequiredArgsConstructor
@CrossOrigin
public class ForumController {
    
    private final ForumService forumService;
    private final UserService userService;
    
    @GetMapping
    public Result<Page<Forum>> getForums(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long categoryId) {
        Page<Forum> page = forumService.getForumPage(pageNum, pageSize, categoryId);
        return Result.success(page);
    }
    
    @GetMapping("/{id}")
    public Result<Forum> getForumDetail(@PathVariable Long id) {
        Forum forum = forumService.getForumDetail(id);
        return Result.success(forum);
    }
    
    @PostMapping
    public Result<Forum> createForum(Authentication authentication, @RequestBody Forum forum) {
        User user = userService.getUserByUsername(authentication.getName());
        forum.setUserId(user.getId());
        forum.setViewCount(0);
        forum.setReplyCount(0);
        forum.setStatus(1);
        forumService.save(forum);
        return Result.success(forum);
    }
}
