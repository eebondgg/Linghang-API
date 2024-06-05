# Linghang-API
一个丰富的API开放调用平台，为开发者提供便捷、实用API调用体验； Java + React 全栈项目，包括网站前台+后台

## 快速上手

### 后端

1. 将各模块配置修改成你自己本地的端口、账号、密码
2. 启动Nacos、Mysql、Redis、RabbitMq
3. 将公共服务 api-common 以及客户端 SDK 安装到本地仓库
4. 按顺序启动服务

服务启动顺序参考：
1. api-backend
2. api-gateway
3. api-interface

### 前端

环境要求：Node.js >= 16

安装依赖：

```
yarn
```

启动：

```
npm run start:dev
```
