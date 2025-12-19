# myMusic
基于 Vue + SpringBoot 开发的轻量级音乐播放器项目，支持音乐播放、收藏、歌单管理、最近播放记录等核心功能，适配 MySQL 8.0.43 环境，开箱即用。

## 克隆到本地
```
git clone git@github.com:niyan566/myMusic.git  # SSH方式（推荐）
# 或 HTTPS方式：git clone https://github.com/niyan566/myMusic.git
cd myMusic # 进入项目根目录
```

## 环境要求
### 后端环境
- JDK 版本：1.8 及以上（推荐 21）
- MySQL 版本：8.0.43（5.7 需替换排序规则，见「常见问题」）
- Maven 版本：3.6+（或 IDEA/Eclipse 自带 Maven）
### 前端环境
- Node.js 版本：最新的稳定版

### 配置文件
```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: 你的用户名
    password: 你的密码
    url: jdbc:mysql://localhost:3306/你的数据库名?autoReconnect=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
```


