package com.kaoyan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.Result;
import com.kaoyan.entity.Resource;
import com.kaoyan.entity.User;
import com.kaoyan.service.ResourceService;
import com.kaoyan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
@CrossOrigin
public class ResourceController {
    
    private final ResourceService resourceService;
    private final UserService userService;
    
    @GetMapping
    public Result<Page<Resource>> getResources(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String category) {
        Page<Resource> page = resourceService.getResourcePage(pageNum, pageSize, category);
        return Result.success(page);
    }
    
    @GetMapping("/{id}")
    public Result<Resource> getResourceDetail(@PathVariable Long id) {
        Resource resource = resourceService.getResourceDetail(id);
        return Result.success(resource);
    }
    
    @PostMapping
    public Result<Resource> addResource(Authentication authentication, @RequestBody Resource resource) {
        User user = userService.getUserByUsername(authentication.getName());
        resource.setUserId(user.getId());
        resourceService.addResource(resource);
        return Result.success(resource);
    }
}
