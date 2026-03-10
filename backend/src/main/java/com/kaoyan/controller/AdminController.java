package com.kaoyan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.Result;
import com.kaoyan.dto.RegisterDTO;
import com.kaoyan.dto.ReviewDTO;
import com.kaoyan.entity.*;
import com.kaoyan.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ResourceService resourceService;
    
    @Autowired
    private ResourceReviewService resourceReviewService;
    
    @Autowired
    private ForumService forumService;
    
    @Autowired
    private SchoolService schoolService;
    
    @Autowired
    private NoticeService noticeService;
    
    @Autowired
    private ResourceCommentService resourceCommentService;
    
    // ==================== 数据统计 ====================
    
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userService.count());
        stats.put("resourceCount", resourceService.count());
        stats.put("forumCount", forumService.count());
        stats.put("noticeCount", noticeService.count());
        
        QueryWrapper<Resource> pendingWrapper = new QueryWrapper<>();
        pendingWrapper.eq("status", 0);
        stats.put("pendingResourceCount", resourceService.count(pendingWrapper));
        
        QueryWrapper<User> studentWrapper = new QueryWrapper<>();
        studentWrapper.eq("role", 1);
        stats.put("studentCount", userService.count(studentWrapper));
        
        QueryWrapper<User> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.eq("role", 2);
        stats.put("teacherCount", userService.count(teacherWrapper));
        
        return Result.success(stats);
    }
    
    // ==================== 用户管理 ====================
    
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Page<User>> getUsers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer role,
            @RequestParam(required = false) Integer status) {
        Page<User> page = userService.getUserPage(pageNum, pageSize, role, status);
        // Clear passwords
        page.getRecords().forEach(u -> u.setPassword(null));
        return Result.success(page);
    }
    
    @PutMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        userService.updateUserStatus(id, body.get("status"));
        return Result.success("操作成功");
    }
    
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success("删除成功");
    }
    
    // ==================== 教师管理 ====================
    
    @GetMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Page<User>> getTeachers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<User> page = userService.getUserPage(pageNum, pageSize, 2, null);
        page.getRecords().forEach(u -> u.setPassword(null));
        return Result.success(page);
    }
    
    @PostMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<User> addTeacher(@RequestBody RegisterDTO dto) {
        try {
            User teacher = userService.createTeacher(dto.getUsername(), dto.getPassword(), dto.getEmail());
            teacher.setPassword(null);
            return Result.success(teacher);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/teachers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> deleteTeacher(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success("删除成功");
    }
    
    // ==================== 资源管理 ====================
    
    @GetMapping("/resources")
    public Result<Page<Resource>> getResources(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status) {
        Page<Resource> page = resourceService.getAllResourcePage(pageNum, pageSize, status);
        return Result.success(page);
    }
    
    @PutMapping("/resources/{id}/review")
    public Result<String> reviewResource(@PathVariable Long id, @RequestBody ReviewDTO dto) {
        resourceReviewService.reviewResource(id, null, dto.getStatus(), dto.getReason());
        return Result.success("审核完成");
    }
    
    @DeleteMapping("/resources/{id}")
    public Result<String> deleteResource(@PathVariable Long id) {
        resourceService.removeById(id);
        return Result.success("删除成功");
    }
    
    // ==================== 论坛管理 ====================
    
    @GetMapping("/forums")
    public Result<Page<Forum>> getForums(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status) {
        Page<Forum> page = forumService.getAllForumPage(pageNum, pageSize, status);
        return Result.success(page);
    }
    
    @PutMapping("/forums/{id}/status")
    public Result<String> updateForumStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        forumService.updateForumStatus(id, body.get("status"));
        return Result.success("操作成功");
    }
    
    @DeleteMapping("/forums/{id}")
    public Result<String> deleteForum(@PathVariable Long id) {
        forumService.removeById(id);
        return Result.success("删除成功");
    }
    
    // ==================== 院校管理 ====================
    
    @PostMapping("/schools")
    public Result<School> addSchool(@RequestBody School school) {
        schoolService.save(school);
        return Result.success(school);
    }
    
    @PutMapping("/schools/{id}")
    public Result<String> updateSchool(@PathVariable Long id, @RequestBody School school) {
        school.setId(id);
        schoolService.updateById(school);
        return Result.success("更新成功");
    }
    
    @DeleteMapping("/schools/{id}")
    public Result<String> deleteSchool(@PathVariable Long id) {
        schoolService.removeById(id);
        return Result.success("删除成功");
    }
    
    // ==================== 公告管理 ====================
    
    @GetMapping("/notices")
    public Result<Page<Notice>> getNotices(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<Notice> page = noticeService.getNoticePage(pageNum, pageSize);
        return Result.success(page);
    }
    
    @PostMapping("/notices")
    public Result<Notice> addNotice(@RequestBody Notice notice) {
        noticeService.save(notice);
        return Result.success(notice);
    }
    
    @PutMapping("/notices/{id}")
    public Result<String> updateNotice(@PathVariable Long id, @RequestBody Notice notice) {
        notice.setId(id);
        noticeService.updateById(notice);
        return Result.success("更新成功");
    }
    
    @DeleteMapping("/notices/{id}")
    public Result<String> deleteNotice(@PathVariable Long id) {
        noticeService.removeById(id);
        return Result.success("删除成功");
    }
    
    // ==================== 资源留言查看 ====================
    
    @GetMapping("/comments")
    public Result<Page<ResourceComment>> getComments(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long resourceId) {
        Page<ResourceComment> page = resourceCommentService.getAllCommentPage(pageNum, pageSize, resourceId);
        return Result.success(page);
    }
}
