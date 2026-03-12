# SweetBite - 情侣美食点餐小程序

基于 Taro + React 的 UniApp 项目

## 📚 文档导航

### 前端文档
- **[📖 文档索引](./INDEX.md)** - 查看所有文档
- **[🎉 最终指南](./FINAL_GUIDE.md)** - 完整使用指南（推荐）
- **[🚀 快速开始](./QUICK_START.md)** - 快速上手指南
- **[✅ 项目完成](./PROJECT_COMPLETE.md)** - 项目完成报告
- **[🏗️ 项目结构](./PROJECT_STRUCTURE.md)** - 详细架构说明

### 后端文档 🆕
- **[🎯 后端总结](./BACKEND_SUMMARY.md)** - 后端接口设计总结（推荐）
- **[📋 API 设计](./backend/API_DESIGN.md)** - 完整 API 接口文档
- **[🔧 开发指南](./backend/DEVELOPMENT_GUIDE.md)** - 后端开发指南
- **[📖 后端说明](./backend/README.md)** - 后端项目说明
- **[💾 数据库脚本](./backend/sql/schema.sql)** - 数据库设计

### 其他文档
- **[✅ 验证清单](./VERIFICATION_CHECKLIST.md)** - 功能验证清单
- **[🐛 问题修复](./BUGFIX.md)** - Bug 修复记录
- **[📝 更新日志](./CHANGELOG.md)** - 版本更新记录

## 项目结构

```
project-root/
├── src/                          # 源代码目录
│   ├── app.config.js            # 应用配置
│   ├── app.jsx                  # 应用入口
│   ├── app.css                  # 全局样式
│   ├── components/              # 公共组件
│   │   ├── Toast/              # 提示组件
│   │   ├── Loader/             # 加载组件
│   │   └── NavItem/            # 导航项组件
│   ├── pages/                   # 页面
│   │   ├── index/              # 主页面（容器）
│   │   ├── home/               # 首页
│   │   ├── menu/               # 菜单页
│   │   ├── cart/               # 购物车页
│   │   ├── shop/               # 商店页
│   │   ├── profile/            # 个人中心
│   │   ├── orders/             # 订单历史
│   │   ├── admin/              # 管理后台
│   │   └── detail/             # 商品详情
│   ├── services/                # 业务逻辑/API
│   │   └── storage.js          # 本地存储服务
│   ├── hooks/                   # 自定义 Hooks
│   │   ├── usePoints.js        # 积分管理
│   │   ├── useCart.js          # 购物车管理
│   │   └── useOrders.js        # 订单管理
│   ├── constants/               # 常量
│   │   └── index.js            # 全局常量
│   ├── types/                   # 类型定义
│   │   └── index.js            # 类型枚举
│   └── utils/                   # 工具函数
│       └── index.js
├── config/                      # 环境配置
│   ├── index.js
│   ├── dev.js
│   └── prod.js
├── dist/                        # 构建输出
├── package.json
├── project.config.json          # 小程序配置
└── babel.config.js
```

## 技术栈

### 前端
- **框架**: Taro 4.x + React 18
- **状态管理**: React Hooks
- **样式**: CSS Modules
- **构建工具**: Webpack 5
- **平台**: 微信小程序 + H5

### 后端 🆕
- **框架**: Spring Boot 2.7.x
- **数据库**: MySQL 8.0
- **ORM**: MyBatis-Plus
- **安全**: Spring Security + JWT
- **缓存**: Redis
- **文档**: Knife4j (Swagger)

## 核心功能模块

### 1. 组件层 (Components)
- `Toast`: 全局提示组件
- `Loader`: 加载动画组件
- `NavItem`: 底部导航项组件

### 2. 页面层 (Pages)
- `HomePage`: 首页，展示推荐菜品和积分
- `MenuPage`: 菜单页，分类浏览所有菜品
- `CartPage`: 购物车，管理待支付商品
- `ShopPage`: 商店页，积分兑换礼品
- `ProfilePage`: 个人中心
- `OrdersPage`: 订单历史
- `AdminPage`: 管理后台
- `DetailPage`: 商品详情

### 3. 业务逻辑层 (Hooks & Services)
- `usePoints`: 积分管理（加载、保存、增减）
- `useCart`: 购物车管理（添加、删除、清空）
- `useOrders`: 订单管理（创建、查询）
- `storageService`: 本地存储服务

### 4. 数据层 (Constants & Types)
- 页面枚举
- 初始菜单数据
- 初始商店数据
- 分类和口味常量

## 开发命令

```bash
# 安装依赖
npm install

# 微信小程序开发
npm run dev:weapp

# H5 开发
npm run dev:h5

# 微信小程序构建
npm run build:weapp

# H5 构建
npm run build:h5
```

## 代码规范

### 组件规范
- 每个组件独立文件夹
- 包含 `index.jsx` 和 `index.css`
- 使用函数式组件 + Hooks
- Props 解构传参

### 样式规范
- 使用 rpx 单位
- 遵循 BEM 命名规范
- 动画使用 CSS Animation

### 状态管理规范
- 页面级状态放在页面组件
- 共享状态使用自定义 Hooks
- 持久化数据使用 storageService

## 项目优势

1. **模块化设计**: 组件、页面、逻辑完全解耦
2. **可维护性强**: 单一职责原则，每个模块功能明确
3. **可扩展性好**: 新增功能只需添加对应模块
4. **代码复用**: 公共组件和 Hooks 可跨页面使用
5. **类型安全**: 统一的类型定义和常量管理

## 后续优化建议

1. 添加 TypeScript 支持
2. 引入状态管理库（如 Zustand）
3. 添加单元测试
4. 接入真实后端 API
5. 添加错误边界处理
6. 性能优化（虚拟列表、图片懒加载）
