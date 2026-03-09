# 大学生考研信息交流平台

一个为考研学生提供信息交流、资源分享、院校查询的一站式平台。

## 🛠 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue3 + Tailwind CSS + Vite |
| 后端 | Spring Boot + Java 8 |
| 数据库 | MySQL |
| 构建工具 | Maven (后端) / Vite (前端) |

## 🏗 项目结构

```
kaoyan-platform/
├── frontend/           # Vue3 前端项目
│   ├── src/
│   │   ├── views/      # 页面视图
│   │   ├── components/ # 组件
│   │   ├── api/        # API 调用
│   │   ├── router/     # 路由
│   │   └── stores/     # 状态管理
│   └── ...
│
├── backend/            # Spring Boot 后端项目
│   ├── src/main/java/com/kaoyan/
│   │   ├── controller/ # 控制器
│   │   ├── service/    # 业务逻辑
│   │   ├── mapper/    # 数据访问
│   │   ├── entity/    # 实体类
│   │   ├── config/    # 配置
│   │   ├── util/      # 工具类
│   │   ├── dto/       # 数据传输对象
│   │   └── common/    # 公共类
│   └── resources/
│       └── application.yml # 配置文件
│
├── database/           # 数据库脚本
│   └── init.sql       # 建表语句
│
└── docs/              # 文档
    └── DESIGN.md      # 设计文档
```

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/你的用户名/kaoyan-platform.git
cd kaoyan-platform
```

### 2. 数据库配置

```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本
source database/init.sql
```

### 3. 后端配置

修改 `backend/src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/kaoyan_platform
    username: your_username
    password: your_password
```

### 4. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端将在 http://localhost:8080 启动

### 5. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端将在 http://localhost:3000 启动

## 📱 功能模块

### 学生模块
- ✅ 注册登录
- ✅ 个人中心
- ✅ 公告信息查看
- ✅ 考研资源查看/下载
- ✅ 交流论坛
- ✅ 院校信息查看
- ✅ 资料上传

### 教师模块
- ✅ 学生管理
- ✅ 考研资料管理
- ✅ 论坛管理
- ✅ 公告管理
- ✅ 院校管理

### 管理员模块
- ✅ 全部管理功能
- ✅ 资料审核
- ✅ 数据分析

## 🔐 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |

## 📄 API 文档

### 认证
- `POST /api/auth/login` - 登录
- `POST /api/auth/register` - 注册
- `GET /api/auth/info` - 获取用户信息

### 院校
- `GET /api/schools` - 院校列表
- `GET /api/schools/{id}` - 院校详情
- `GET /api/schools/provinces` - 获取所有省份

### 资源
- `GET /api/resources` - 资源列表
- `GET /api/resources/{id}` - 资源详情
- `POST /api/resources` - 上传资源

### 论坛
- `GET /api/forums` - 帖子列表
- `GET /api/forums/{id}` - 帖子详情
- `POST /api/forums` - 发布帖子

### 公告
- `GET /api/notices` - 公告列表

## 🧪 开发说明

本项目采用前后端分离架构：
- 前端使用 Vue3 + Tailwind CSS 构建响应式 UI
- 后端使用 Spring Boot + MyBatis Plus
- 使用 JWT 进行身份认证

## 📝 许可证

MIT License
