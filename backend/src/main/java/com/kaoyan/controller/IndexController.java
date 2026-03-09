package com.kaoyan.controller;

import com.kaoyan.common.Result;
import com.kaoyan.entity.Category;
import com.kaoyan.entity.Notice;
import com.kaoyan.service.CategoryService;
import com.kaoyan.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class IndexController {
    
    private final NoticeService noticeService;
    private final CategoryService categoryService;
    
    @GetMapping("/notices")
    public Result<List<Notice>> getNotices() {
        List<Notice> notices = noticeService.getNotices();
        return Result.success(notices);
    }
    
    @GetMapping("/categories")
    public Result<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        return Result.success(categories);
    }
    
    @GetMapping("/forum-categories")
    public Result<List<Category>> getForumCategories() {
        List<Category> categories = categoryService.getForumCategories();
        return Result.success(categories);
    }
}
