# 大学生考研信息交流平台 - 设计文档

## 1. 项目概述

- **项目名称**：大学生考研信息交流平台
- **项目类型**：前后端分离 Web 应用
- **核心功能**：为大学生提供考研信息交流、资源分享、院校查询的一站式平台
- **目标用户**：考研学生、管理员、教师

## 2. 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue3 + Tailwind CSS |
| 后端 | Spring Boot + Java 8 |
| 数据库 | MySQL |
| 构建工具 | Maven (后端) / Vite (前端) |

## 3. 系统架构

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Vue3      │ ──▶ │  Spring     │ ──▶ │   MySQL     │
│  Frontend   │     │   Boot      │     │  Database   │
└─────────────┘     └─────────────┘     └─────────────┘
      │                   │                   │
   :3000              :8080                :3306
```

## 4. 数据库设计

### 4.1 用户表 (users)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名 |
| password | VARCHAR(255) | 密码 |
| email | VARCHAR(100) | 邮箱 |
| phone | VARCHAR(20) | 手机号 |
| role | TINYINT | 角色：1-学生 2-教师 3-管理员 |
| status | TINYINT | 状态：0-禁用 1-正常 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

### 4.2 用户详情表 (user_profiles)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| real_name | VARCHAR(50) | 真实姓名 |
| avatar | VARCHAR(255) | 头像URL |
| gender | TINYINT | 性别：0-未知 1-男 2-女 |
| school | VARCHAR(100) | 毕业学校 |
| major | VARCHAR(100) | 专业 |
| bio | TEXT | 个人简介 |

### 4.3 院校分类表 (categories)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 分类名称 |
| parent_id | BIGINT | 父分类ID |
| sort_order | INT | 排序 |

### 4.4 院校信息表 (schools)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 院校名称 |
| province | VARCHAR(50) | 所在省份 |
| city | VARCHAR(50) | 所在城市 |
| category_id | BIGINT | 分类ID |
| rank | INT | 排名 |
| logo | VARCHAR(255) | 校徽 |
| description | TEXT | 简介 |
| admission_rate | DECIMAL(5,2) | 录取率 |
| created_at | DATETIME | 创建时间 |

### 4.5 考研资源表 (resources)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(200) | 标题 |
| category | VARCHAR(50) | 分类：真题/笔记/经验/分数线 |
| content | TEXT | 内容 |
| file_url | VARCHAR(255) | 文件URL |
| user_id | BIGINT | 发布者ID |
| view_count | INT | 浏览量 |
| download_count | INT | 下载量 |
| status | TINYINT | 状态：0-待审核 1-已发布 |
| created_at | DATETIME | 创建时间 |

### 4.6 资源评论表 (resource_comments)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| resource_id | BIGINT | 资源ID |
| user_id | BIGINT | 评论者ID |
| content | TEXT | 评论内容 |
| created_at | DATETIME | 创建时间 |

### 4.7 论坛帖子表 (forums)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(200) | 标题 |
| content | TEXT | 内容 |
| category_id | BIGINT | 分类ID |
| user_id | BIGINT | 作者ID |
| view_count | INT | 浏览量 |
| reply_count | INT | 回复数 |
| status | TINYINT | 状态：0-隐藏 1-正常 |
| created_at | DATETIME | 创建时间 |

### 4.8 论坛回复表 (forum_replies)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| forum_id | BIGINT | 帖子ID |
| user_id | BIGINT | 回复者ID |
| parent_id | BIGINT | 父回复ID |
| content | TEXT | 内容 |
| created_at | DATETIME | 创建时间 |

### 4.9 公告表 (notices)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(200) | 标题 |
| content | TEXT | 内容 |
| user_id | BIGINT | 发布者ID |
| is_top | TINYINT | 是否置顶 |
| created_at | DATETIME | 创建时间 |

### 4.10 资料审核表 (resource_reviews)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| resource_id | BIGINT | 资源ID |
| reviewer_id | BIGINT | 审核者ID |
| status | TINYINT | 状态：0-拒绝 1-通过 |
| reason | VARCHAR(500) | 原因 |
| created_at | DATETIME | 创建时间 |

## 5. API 设计

### 5.1 认证模块
- POST /api/auth/register - 注册
- POST /api/auth/login - 登录
- GET /api/auth/info - 获取用户信息

### 5.2 用户模块
- GET /api/users/:id - 获取用户信息
- PUT /api/users/:id - 更新用户信息

### 5.3 院校模块
- GET /api/schools - 院校列表
- GET /api/schools/:id - 院校详情
- POST /api/schools - 添加院校（管理员）
- PUT /api/schools/:id - 更新院校（管理员）
- DELETE /api/schools/:id - 删除院校（管理员）

### 5.4 资源模块
- GET /api/resources - 资源列表
- GET /api/resources/:id - 资源详情
- POST /api/resources - 发布资源
- DELETE /api/resources/:id - 删除资源
- GET /api/resources/:id/comments - 资源评论

### 5.5 论坛模块
- GET /api/forums - 帖子列表
- GET /api/forums/:id - 帖子详情
- POST /api/forums - 发布帖子
- POST /api/forums/:id/replies - 回复帖子

### 5.6 公告模块
- GET /api/notices - 公告列表
- POST /api/notices - 发布公告（管理员）

## 6. 功能模块

### 6.1 学生模块
- 注册登录
- 个人中心（查看/修改个人信息）
- 公告信息查看
- 考研资源查看/下载
- 交流论坛（发帖/回复）
- 资源留言
- 院校信息查看
- 资料上传

### 6.2 教师模块
- 个人中心
- 学生管理
- 考研资料管理
- 交流论坛管理
- 公告信息管理
- 考研资源留言查看
- 院校信息管理

### 6.3 管理员模块
- 个人中心
- 学生管理
- 资料审核
- 考研资源管理
- 交流论坛管理
- 教师管理
- 公告信息管理
- 数据分析
- 院校信息管理

## 7. 安全性设计

- 密码使用 BCrypt 加密存储
- JWT Token 鉴权
- 接口权限控制
- SQL 注入防护
- XSS 防护

## 8. 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| v1.0.0 | 2026-03-09 | 初始版本 |
