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

import java.util.Collections;

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
}
