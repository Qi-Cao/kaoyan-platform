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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    
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
            data.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole()
            ));
            
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("用户名或密码错误");
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
}
