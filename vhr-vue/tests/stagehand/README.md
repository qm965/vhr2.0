# Stagehand 业务探索测试

本目录是 vhr2.0 的 AI 辅助业务探索层。它与确定性的回归测试刻意分离：Stagehand 用于探索尚不熟悉的界面并记录业务路径；确认后的预期结果再转写为稳定的 Playwright 测试。

## 安全规则

- 只能在本地可清理的数据库和测试账号上运行。
- 探索脚本不得创建、编辑、删除、审批或提交业务数据。
- 后续任何写入型流程，都必须在第一次写入前检查 `VHR_STAGEHAND_ALLOW_WRITE=true`，并在结束后清理测试数据。
- 模型推断出的业务含义仅是待验证假设；在将其变为回归断言前，必须通过 vhr 源码、接口响应和数据库变化进行核实。

## 运行菜单发现测试

Stagehand 3 需要 Node.js `^20.19.0 || >=22.12.0`。测试依赖本地 MySQL、后端和 Vite 前端服务；请在项目根目录下的不同终端中分别执行：

```bash
# 终端 1：本地 MySQL、Redis、RabbitMQ（首次运行需先配置 .env）
cd dev-infra
cp .env.example .env
docker compose up -d

# 终端 2：Spring Boot 后端
cd ../vhr
mvn -pl vhr-web -am package -DskipTests
java -jar vhr-web/target/vhr-web-0.0.2.jar

# 终端 3：Vue 前端
cd ../vhr-vue
npm ci
npm run dev
```

当前端可通过 `http://127.0.0.1:5173` 访问后，在另一个终端中配置并运行探索脚本：

```bash
cd vhr-vue
cp stagehand.env.example .env.stagehand
# 编辑 .env.stagehand，填写 DEEPSEEK_API_KEY 和本地一次性测试账号。
npm run test:stagehand:discover
```

若 `.env.stagehand` 已存在，请不要再次复制覆盖，直接编辑已有文件即可。只有 Vite 使用非默认地址时，才需设置 `VHR_BASE_URL`。

`.env.stagehand` 已被 Git 忽略，绝不能提交。受版本控制的 `stagehand.env.example` 不含真实 Key。默认模型为 `deepseek/deepseek-flash`；仅在有意切换模型时修改 `VHR_STAGEHAND_MODEL`。

`discover-login-and-menus.mjs` 会登录并输出当前可见菜单及其推断的业务职责 JSON，是构建角色—模块映射的入口，后续可在此基础上继续探索员工生命周期、人事异动和薪资流程。

脚本对登录表单使用确定性的本地浏览器操作，因此测试用户名和密码不会发送给 DeepSeek。登录后的菜单快照和探索指令会发送给 DeepSeek 进行分析；输出不是事实来源，必须经人工或源码验证。
