-- 大学生考研信息交流平台 - 数据库初始化脚本
-- 创建日期: 2026-03-09

-- 创建数据库
CREATE DATABASE IF NOT EXISTS kaoyan DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE kaoyan;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    role TINYINT NOT NULL DEFAULT 1 COMMENT '角色：1-学生 2-教师 3-管理员',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 用户详情表
CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    real_name VARCHAR(50) COMMENT '真实姓名',
    avatar VARCHAR(255) DEFAULT '/images/default-avatar.png' COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知 1-男 2-女',
    school VARCHAR(100) COMMENT '毕业学校',
    major VARCHAR(100) COMMENT '专业',
    bio TEXT COMMENT '个人简介',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户详情表';

-- 3. 院校分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    sort_order INT DEFAULT 0 COMMENT '排序',
    INDEX idx_parent (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='院校分类表';

-- 4. 院校信息表
CREATE TABLE IF NOT EXISTS schools (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(100) NOT NULL COMMENT '院校名称',
    province VARCHAR(50) COMMENT '所在省份',
    city VARCHAR(50) COMMENT '所在城市',
    category_id BIGINT COMMENT '分类ID',
    rank INT COMMENT '排名',
    logo VARCHAR(255) COMMENT '校徽',
    description TEXT COMMENT '简介',
    admission_rate DECIMAL(5,2) COMMENT '录取率',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (category_id) REFERENCES categories(id),
    INDEX idx_province (province),
    INDEX idx_rank (rank)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='院校信息表';

-- 5. 考研资源表
CREATE TABLE IF NOT EXISTS resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    category VARCHAR(50) NOT NULL COMMENT '分类：真题/笔记/经验/分数线',
    content TEXT COMMENT '内容',
    file_url VARCHAR(255) COMMENT '文件URL',
    user_id BIGINT NOT NULL COMMENT '发布者ID',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    download_count INT DEFAULT 0 COMMENT '下载量',
    status TINYINT DEFAULT 0 COMMENT '状态：0-待审核 1-已发布',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_category (category),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考研资源表';

-- 6. 资源评论表
CREATE TABLE IF NOT EXISTS resource_comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    user_id BIGINT NOT NULL COMMENT '评论者ID',
    content TEXT NOT NULL COMMENT '评论内容',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源评论表';

-- 7. 论坛帖子表
CREATE TABLE IF NOT EXISTS forums (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    category_id BIGINT COMMENT '分类ID',
    user_id BIGINT NOT NULL COMMENT '作者ID',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    reply_count INT DEFAULT 0 COMMENT '回复数',
    status TINYINT DEFAULT 1 COMMENT '状态：0-隐藏 1-正常',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    INDEX idx_category (category_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子表';

-- 8. 论坛回复表
CREATE TABLE IF NOT EXISTS forum_replies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    forum_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '回复者ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父回复ID',
    content TEXT NOT NULL COMMENT '内容',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (forum_id) REFERENCES forums(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_forum (forum_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛回复表';

-- 9. 公告表
CREATE TABLE IF NOT EXISTS notices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    user_id BIGINT COMMENT '发布者ID',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_is_top (is_top)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 10. 资料审核表
CREATE TABLE IF NOT EXISTS resource_reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    reviewer_id BIGINT COMMENT '审核者ID',
    status TINYINT DEFAULT 0 COMMENT '状态：0-拒绝 1-通过',
    reason VARCHAR(500) COMMENT '原因',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (resource_id) REFERENCES resources(id),
    FOREIGN KEY (reviewer_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资料审核表';

-- 插入默认管理员账号 (密码: admin123)
-- 使用BCrypt加密: admin123 -> $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi
INSERT INTO users (username, password, email, role, status) 
VALUES ('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@kaoyan.com', 3, 1);

-- 插入默认管理员详情
INSERT INTO user_profiles (user_id, real_name, avatar)
SELECT id, '系统管理员', '/images/admin-avatar.png' FROM users WHERE username = 'admin';

-- 插入默认分类
INSERT INTO categories (name, parent_id, sort_order) VALUES
('综合类', 0, 1),
('理工类', 0, 2),
('文科类', 0, 3),
('师范类', 0, 4),
('医学类', 0, 5);

-- 插入示例院校
INSERT INTO schools (name, province, city, category_id, rank, description, admission_rate) VALUES
('北京大学', '北京', '北京', 1, 1, '中国顶尖综合性大学', 1.85),
('清华大学', '北京', '北京', 2, 2, '中国顶尖理工类大学', 1.92),
('复旦大学', '上海', '上海', 1, 3, '中国顶尖综合性大学', 2.15),
('上海交通大学', '上海', '上海', 2, 4, '中国顶尖理工类大学', 2.32),
('浙江大学', '浙江', '杭州', 1, 5, '中国顶尖综合性大学', 2.45);

-- 插入示例公告
INSERT INTO notices (title, content, user_id, is_top) VALUES
('欢迎使用考研信息交流平台', '本平台为广大考研学子提供全面的信息交流服务，祝大家考研顺利！', 1, 1),
('平台使用说明', '请各位用户文明发言，共同维护良好的交流环境。', 1, 0);

-- 插入示例论坛分类
INSERT INTO categories (name, parent_id, sort_order) VALUES
('考研经验', 0, 10),
('专业课讨论', 0, 11),
('公共课交流', 0, 12),
('资料分享', 0, 13),
('情感树洞', 0, 14);

SELECT '数据库初始化完成！' AS message;
