package com.kaoyan.controller;

import com.kaoyan.common.Result;
import com.kaoyan.dto.PasswordDTO;
import com.kaoyan.entity.User;
import com.kaoyan.entity.UserProfile;
import com.kaoyan.service.UserProfileService;
import com.kaoyan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/profile")
    public Result<Map<String, Object>> getProfile(Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        UserProfile profile = userProfileService.getProfileByUserId(user.getId());
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("phone", user.getPhone());
        data.put("role", user.getRole());
        
        if (profile != null) {
            data.put("realName", profile.getRealName());
            data.put("avatar", profile.getAvatar());
            data.put("gender", profile.getGender());
            data.put("school", profile.getSchool());
            data.put("major", profile.getMajor());
            data.put("bio", profile.getBio());
        }
        
        return Result.success(data);
    }
    
    @PutMapping("/profile")
    public Result<String> updateProfile(Authentication authentication, @RequestBody Map<String, Object> body) {
        User user = userService.getUserByUsername(authentication.getName());
        
        // Update user basic info
        if (body.containsKey("email")) {
            user.setEmail((String) body.get("email"));
        }
        if (body.containsKey("phone")) {
            user.setPhone((String) body.get("phone"));
        }
        userService.updateById(user);
        
        // Update profile
        UserProfile profile = userProfileService.getProfileByUserId(user.getId());
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(user.getId());
        }
        if (body.containsKey("realName")) {
            profile.setRealName((String) body.get("realName"));
        }
        if (body.containsKey("avatar")) {
            profile.setAvatar((String) body.get("avatar"));
        }
        if (body.containsKey("gender")) {
            profile.setGender((Integer) body.get("gender"));
        }
        if (body.containsKey("school")) {
            profile.setSchool((String) body.get("school"));
        }
        if (body.containsKey("major")) {
            profile.setMajor((String) body.get("major"));
        }
        if (body.containsKey("bio")) {
            profile.setBio((String) body.get("bio"));
        }
        userProfileService.saveOrUpdateProfile(profile);
        
        return Result.success("更新成功");
    }
    
    @PutMapping("/password")
    public Result<String> updatePassword(Authentication authentication, @RequestBody PasswordDTO dto) {
        User user = userService.getUserByUsername(authentication.getName());
        
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return Result.error("原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userService.updateById(user);
        
        return Result.success("密码修改成功");
    }
}
