package com.kaoyan.controller;

import com.kaoyan.common.Result;
import com.kaoyan.dto.LoginDTO;
import com.kaoyan.dto.RegisterDTO;
import com.kaoyan.entity.User;
import com.kaoyan.service.UserService;
import com.kaoyan.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(),
                    loginDTO.getPassword()
                )
            );
            
            String token = jwtUtil.generateToken(loginDTO.getUsername());
            User user = userService.getUserByUsername(loginDTO.getUsername());
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("role", user.getRole());
            String roleName = user.getRole() == 1 ? "STUDENT" : 
                             user.getRole() == 2 ? "TEACHER" : "ADMIN";
            userMap.put("roleName", roleName);
            data.put("user", userMap);
            
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("用户名或密码错误: " + e.getMessage());
        }
    }
    
    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterDTO registerDTO) {
        try {
            User user = userService.register(
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                registerDTO.getEmail()
            );
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/info")
    public Result<User> getUserInfo(Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        user.setPassword(null);
        return Result.success(user);
    }
    
    // 临时接口：生成 BCrypt 密码
    @GetMapping("/hash")
    public Result<String> generateHash(@RequestParam String password) {
        String hash = passwordEncoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
        return Result.success(hash);
    }
}
