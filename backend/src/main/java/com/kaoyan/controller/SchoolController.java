package com.kaoyan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaoyan.common.Result;
import com.kaoyan.entity.School;
import com.kaoyan.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
@CrossOrigin
public class SchoolController {
    
    private final SchoolService schoolService;
    
    @GetMapping
    public Result<Page<School>> getSchools(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String keyword) {
        Page<School> page = schoolService.getSchoolPage(pageNum, pageSize, province, keyword);
        return Result.success(page);
    }
    
    @GetMapping("/{id}")
    public Result<School> getSchoolDetail(@PathVariable Long id) {
        School school = schoolService.getById(id);
        return Result.success(school);
    }
    
    @GetMapping("/provinces")
    public Result<List<String>> getProvinces() {
        List<String> provinces = schoolService.getAllProvinces();
        return Result.success(provinces);
    }
}
