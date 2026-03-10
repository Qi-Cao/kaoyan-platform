package com.kaoyan.controller;

import com.kaoyan.common.Result;
import com.kaoyan.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
public class FileController {
    
    @Autowired
    private FileService fileService;
    
    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        try {
            String url = fileService.upload(file);
            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            data.put("originalName", file.getOriginalFilename());
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }
}
