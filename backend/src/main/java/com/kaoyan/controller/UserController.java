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

    // ========== 管理员/教师端接口 ==========

    /**
     * 获取用户列表（管理员/教师）
     * 教师只能查看学生列表，管理员可以查看所有用户
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getUserList(
            Authentication authentication,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer role) {
        // 检查权限（管理员或教师）
        User currentUser = userService.getUserByUsername(authentication.getName());
        if (currentUser.getRole() != 2 && currentUser.getRole() != 3) {
            return Result.error("权限不足");
        }

        // 教师只能查看学生，不能查看其他教师和管理员
        if (currentUser.getRole() == 2) {
            role = 1; // 强制设置为学生
        }

        Map<String, Object> result = userService.getUserList(pageNum, pageSize, username, role);
        return Result.success(result);
    }

    /**
     * 获取用户详情（管理员/教师）
     * 教师只能查看学生详情
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getUserDetail(Authentication authentication, @PathVariable Long id) {
        // 检查权限
        User currentUser = userService.getUserByUsername(authentication.getName());
        if (currentUser.getRole() != 2 && currentUser.getRole() != 3) {
            return Result.error("权限不足");
        }

        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 教师不能查看教师或管理员的详情
        if (currentUser.getRole() == 2 && user.getRole() != 1) {
            return Result.error("权限不足，无法查看其他教师或管理员的信息");
        }

        UserProfile profile = userProfileService.getProfileByUserId(id);

        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("phone", user.getPhone());
        data.put("role", user.getRole());
        data.put("status", user.getStatus());
        data.put("createdAt", user.getCreatedAt());

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

    /**
     * 修改用户信息（管理员/教师）
     * 教师只能修改学生信息，不能修改其他教师或管理员
     */
    @PutMapping("/{id}")
    public Result<String> updateUserByAdmin(Authentication authentication, @PathVariable Long id, @RequestBody Map<String, Object> body) {
        // 检查权限
        User currentUser = userService.getUserByUsername(authentication.getName());
        if (currentUser.getRole() != 2 && currentUser.getRole() != 3) {
            return Result.error("权限不足");
        }

        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 教师不能修改教师或管理员
        if (currentUser.getRole() == 2 && user.getRole() != 1) {
            return Result.error("权限不足，无法修改其他教师或管理员的信息");
        }

        // 修改基本信息
        if (body.containsKey("email")) {
            user.setEmail((String) body.get("email"));
        }
        if (body.containsKey("phone")) {
            user.setPhone((String) body.get("phone"));
        }
        // 修改角色（管理员才能修改）
        if (body.containsKey("role") && currentUser.getRole() == 3) {
            user.setRole((Integer) body.get("role"));
        }
        // 修改状态（管理员才能修改）
        if (body.containsKey("status") && currentUser.getRole() == 3) {
            user.setStatus((Integer) body.get("status"));
        }
        userService.updateById(user);

        // 修改详细信息
        UserProfile profile = userProfileService.getProfileByUserId(id);
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(id);
        }
        if (body.containsKey("realName")) {
            profile.setRealName((String) body.get("realName"));
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

    /**
     * 重置用户密码（管理员）
     */
    @PutMapping("/{id}/reset-password")
    public Result<String> resetPassword(Authentication authentication, @PathVariable Long id) {
        // 只有管理员可以重置密码
        User currentUser = userService.getUserByUsername(authentication.getName());
        if (currentUser.getRole() != 3) {
            return Result.error("权限不足，仅管理员可重置密码");
        }

        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 重置为默认密码 "123456"
        user.setPassword(passwordEncoder.encode("123456"));
        userService.updateById(user);

        return Result.success("密码已重置为: 123456");
    }

    /**
     * 修改用户密码（管理员/教师）
     * 教师只能修改学生密码，不能修改其他教师或管理员
     */
    @PutMapping("/{id}/password")
    public Result<String> updateUserPassword(Authentication authentication, @PathVariable Long id, @RequestBody Map<String, Object> body) {
        // 检查权限
        User currentUser = userService.getUserByUsername(authentication.getName());
        if (currentUser.getRole() != 2 && currentUser.getRole() != 3) {
            return Result.error("权限不足");
        }

        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 教师不能修改教师或管理员的密码
        if (currentUser.getRole() == 2 && user.getRole() != 1) {
            return Result.error("权限不足，无法修改其他教师或管理员的密码");
        }

        String newPassword = (String) body.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return Result.error("新密码不能为空");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateById(user);

        return Result.success("密码修改成功");
    }

    /**
     * 修改用户状态（管理员）
     * 教师只能启用/禁用学生
     */
    @PutMapping("/{id}/status")
    public Result<String> updateUserStatus(Authentication authentication, @PathVariable Long id, @RequestBody Map<String, Object> body) {
        // 检查权限
        User currentUser = userService.getUserByUsername(authentication.getName());
        if (currentUser.getRole() != 2 && currentUser.getRole() != 3) {
            return Result.error("权限不足");
        }

        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 教师不能修改教师或管理员的状态
        if (currentUser.getRole() == 2 && user.getRole() != 1) {
            return Result.error("权限不足，无法修改其他教师或管理员的状态");
        }

        Integer status = (Integer) body.get("status");
        user.setStatus(status);
        userService.updateById(user);

        return Result.success("状态更新成功");
    }

    /**
     * 删除用户（管理员）
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(Authentication authentication, @PathVariable Long id) {
        // 只有管理员可以删除用户
        User currentUser = userService.getUserByUsername(authentication.getName());
        if (currentUser.getRole() != 3) {
            return Result.error("权限不足，仅管理员可删除用户");
        }

        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 不能删除自己
        if (user.getId().equals(currentUser.getId())) {
            return Result.error("不能删除自己的账号");
        }

        userService.removeById(id);
        return Result.success("删除成功");
    }
}
