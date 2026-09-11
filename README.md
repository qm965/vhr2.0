## vhr2.0 版

此版本使用 SpringBoot3+Vue3 重构 vhr，业务代码变化不大，脚手架上有一些差异，因此这个版本专注于脚手架在搭建时候的变化。

另需注意，相比于 vhr 1.0 版，2.0 引入了 MyBatis Plus，项目模块划分也和 1.0 版有差异。

本版本亦配有付费视频教程，有需要可扫码关注微信公众号后回复vhr查看介绍。

![](https://open.weixin.qq.com/qr/code?username=a_javaboy)

## 本地运行

本项目由 Spring Boot 后端和 Vue/Vite 前端组成。需要 Java 17、Maven、Node.js
`^20.19.0 || >=22.12.0` 以及 Docker Desktop（用于本地依赖服务）。

### 1. 启动本地依赖

默认配置使用 MySQL（`localhost:33061`，数据库 `vhr2024`，账号 `root`，密码
`123`）。在项目根目录执行：

```bash
cd dev-infra
cp .env.example .env
docker compose up -d
```

若这是全新的 MySQL 数据卷，导入初始数据：

```bash
docker compose exec -T mysql mysql -uroot -p123 vhr2024 < ../vhr/vhr2024_2024-01-09.sql
```

已有数据库时无需重复导入。数据库连接配置位于
`vhr/vhr-web/src/main/resources/application.yaml`；修改账号、密码或端口时，请同时
更新该文件与 `dev-infra/.env`。

### 2. 启动后端

在一个终端中执行：

```bash
cd vhr
mvn -pl vhr-web -am package -DskipTests
java -jar vhr-web/target/vhr-web-0.0.2.jar
```

后端默认监听 `http://localhost:8080`。使用 `Ctrl+C` 停止。

### 3. 启动前端

在另一个终端中执行：

```bash
cd vhr-vue
npm ci
npm run dev
```

Vite 会显示访问地址；本项目固定使用 `http://127.0.0.1:5173`，并将 `/api` 请求代理到
后端 `http://localhost:8080`。使用 `Ctrl+C` 停止。

### 常用校验

```bash
cd vhr && mvn -DskipTests verify
cd ../vhr-vue && npm run build
```

## AI 业务探索测试

前端已集成 Stagehand（默认使用 DeepSeek），用于在业务尚未完全梳理时探索实际页面、记录业务步骤并生成
后续稳定回归测试的候选场景。完整的启动前置条件、环境配置、运行命令和数据安全边界见
[`vhr-vue/tests/stagehand/README.md`](vhr-vue/tests/stagehand/README.md)。
