package com.kaoyan;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("admin123");
        System.out.println("admin123 的 BCrypt 哈希: " + hash);

        // 验证
        boolean matches = encoder.matches("admin123", hash);
        System.out.println("验证结果: " + matches);
    }
}
