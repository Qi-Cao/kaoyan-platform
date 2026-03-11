package com.kaoyan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaoyan.entity.User;
import com.kaoyan.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> implements UserDetailsService {
    
    private final PasswordEncoder passwordEncoder;
    
    public User register(String username, String password, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        if (this.count(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(1); // 默认学生
        user.setStatus(1);
        
        this.save(user);
        return user;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = this.getOne(queryWrapper);
        
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        
        String roleName = user.getRole() == 1 ? "STUDENT" : 
                         user.getRole() == 2 ? "TEACHER" : "ADMIN";
        
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.getStatus() == 1,
            true, true, true,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName))
        );
    }
    
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return this.getOne(queryWrapper);
    }
    
    public Page<User> getUserPage(int pageNum, int pageSize, Integer role, Integer status) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (role != null) {
            queryWrapper.eq("role", role);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("created_at");
        return this.page(page, queryWrapper);
    }
    
    public boolean updateUserStatus(Long id, Integer status) {
        User user = this.getById(id);
        if (user != null) {
            user.setStatus(status);
            return this.updateById(user);
        }
        return false;
    }
    
    public User createTeacher(String username, String password, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        if (this.count(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(2);
        user.setStatus(1);
        this.save(user);
        return user;
    }

    /**
     * 获取用户列表（分页）
     */
    public Map<String, Object> getUserList(Integer pageNum, Integer pageSize, String username, Integer role) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            queryWrapper.like("username", username);
        }
        if (role != null) {
            queryWrapper.eq("role", role);
        }
        queryWrapper.orderByDesc("created_at");

        Page<User> result = this.page(page, queryWrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("pages", result.getPages());
        data.put("current", result.getCurrent());
        data.put("size", result.getSize());

        return data;
    }
}
