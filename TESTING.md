# 自动化测试说明

## 测试数据约定

本项目直接使用 `dev-infra` 中的 `vhr2024` 开发测试库。它不是生产库：自动化测试可以新增带时间戳的基础资料，以验证菜单、权限和关联数据的完整链路。不得将任何生产地址、生产账号或生产数据库配置用于测试。

## 后端业务规则测试

```bash
cd vhr
mvn -pl vhr-web -am test
```

当前第一批覆盖部门层级新增、父部门删除限制，以及职位新增与重名拦截。测试写入的数据会保留在开发测试库中。

首次执行还会创建或更新仅供自动化使用的 `automation-test` 管理员账号；密码由本地 `.env.e2e` 配置，绝不提交到 Git。

## 前端 Playwright 回归

先按根目录 README 启动开发测试库和后端，再运行：

```bash
cd vhr-vue
cp .env.e2e.example .env.e2e
# 编辑 .env.e2e：确认后端地址、测试账号和 VHR_E2E_ALLOW_WRITE=true。
npm run test:e2e
```

Playwright 会自动在 `5174` 启动测试前端，并将 `/api` 代理到 `.env.e2e` 中的后端地址。写操作默认跳过，只有显式设定 `VHR_E2E_ALLOW_WRITE=true` 才会运行。报告可通过 `npm run test:e2e:report` 查看。

## 一键执行公共测试底座

确保后端已按 README 在 `8080` 启动，并填写 `vhr-vue/.env.e2e` 与 `vhr-vue/.env.stagehand` 后执行：

```bash
bash scripts/test-foundation.sh
```

该命令依次执行后端业务与 HTTP 测试、Playwright 基础信息回归，以及 Stagehand 基础信息只读探索。它会在开发测试库中保留自动化数据；Stagehand 会将登录后的页面可见信息发送给配置的模型服务。

## Stagehand 探索测试

Stagehand 用于只读业务探索，可运行 `npm run test:stagehand:discover` 或 `npm run test:stagehand:system-basic`。包含测试账号或模型密钥的 `.env.stagehand` 已被 Git 忽略，不得提交。
