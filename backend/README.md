# SweetBite 后端接口文档

## 技术栈

- **框架**: Spring Boot 2.7.x
- **数据库**: MySQL 8.0
- **ORM**: MyBatis-Plus
- **安全**: Spring Security + JWT
- **文档**: Swagger/Knife4j
- **缓存**: Redis
- **工具**: Lombok, Hutool

## 项目结构

```
backend/
├── src/main/java/com/sweetbite/
│   ├── SweetBiteApplication.java
│   ├── config/                 # 配置类
│   ├── controller/             # 控制器
│   ├── service/                # 服务层
│   ├── mapper/                 # 数据访问层
│   ├── entity/                 # 实体类
│   ├── dto/                    # 数据传输对象
│   ├── vo/                     # 视图对象
│   ├── common/                 # 公共类
│   ├── exception/              # 异常处理
│   └── utils/                  # 工具类
├── src/main/resources/
│   ├── application.yml
│   ├── application-dev.yml
│   ├── application-prod.yml
│   └── mapper/                 # MyBatis XML
└── pom.xml
```

## 数据库设计

### 核心表

1. **couple_account** - 情侣账户表
2. **user** - 用户表
3. **food** - 食物表
4. **gift** - 礼品表
5. **order** - 订单表
6. **order_item** - 订单明细表
7. **points_log** - 积分流水表

## API 接口

### 基础路径
```
http://localhost:8080/api/v1
```

### 认证接口
- POST /auth/login - 登录
- POST /auth/register - 注册
- POST /auth/logout - 登出
- GET /auth/info - 获取用户信息

### 情侣账户接口
- GET /couple/info - 获取情侣账户信息
- PUT /couple/update - 更新情侣信息
- GET /couple/points - 获取积分余额
- GET /couple/points/log - 获取积分流水

### 食物接口
- GET /food/list - 获取食物列表
- GET /food/{id} - 获取食物详情
- GET /food/category/{category} - 按分类获取食物
- POST /food/create - 创建食物（管理员）
- PUT /food/update - 更新食物（管理员）
- DELETE /food/{id} - 删除食物（管理员）

### 礼品接口
- GET /gift/list - 获取礼品列表
- GET /gift/{id} - 获取礼品详情
- POST /gift/redeem - 兑换礼品
- POST /gift/create - 创建礼品（管理员）
- PUT /gift/update - 更新礼品（管理员）
- DELETE /gift/{id} - 删除礼品（管理员）

### 订单接口
- POST /order/create - 创建订单
- GET /order/list - 获取订单列表
- GET /order/{id} - 获取订单详情
- PUT /order/cancel - 取消订单

### 管理员接口
- POST /admin/points/grant - 发放积分
- GET /admin/statistics - 获取统计数据
- GET /admin/orders - 获取所有订单

## 启动说明

1. 创建数据库
```sql
CREATE DATABASE sweetbite DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件 `application-dev.yml`

3. 运行项目
```bash
mvn spring-boot:run
```

4. 访问接口文档
```
http://localhost:8080/doc.html
```
