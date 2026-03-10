package com.kaoyan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.Result;
import com.kaoyan.entity.ForumReply;
import com.kaoyan.entity.User;
import com.kaoyan.service.ForumReplyService;
import com.kaoyan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forum-replies")
@CrossOrigin
public class ForumReplyController {
    
    @Autowired
    private ForumReplyService forumReplyService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public Result<Page<ForumReply>> getReplies(
            @RequestParam Long forumId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<ForumReply> page = forumReplyService.getReplyPage(forumId, pageNum, pageSize);
        return Result.success(page);
    }
    
    @PostMapping
    public Result<ForumReply> addReply(Authentication authentication, @RequestBody ForumReply reply) {
        User user = userService.getUserByUsername(authentication.getName());
        reply.setUserId(user.getId());
        forumReplyService.addReply(reply);
        return Result.success(reply);
    }
}
