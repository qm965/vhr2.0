# vhr2.0 依赖升级说明

本次将直接声明的第三方依赖升级至 2026-09-11 核对到的最新稳定版本。前端版本使用精确版本号而非范围，以确保全新安装可复现；生成的 `package-lock.json` 记录了传递依赖。

## 后端

- Spring Boot：`3.2.1` 升级至 `4.0.3`。Spring Boot 4 仍可运行于项目使用的 Java 17。Servlet Web Starter 改为 `spring-boot-starter-webmvc`，旧的 `spring-boot-starter-web` 已废弃。
- MyBatis-Plus：`3.5.5` 升级至 `3.5.16`。旧的通用 `mybatis-plus-boot-starter` 改为 Spring Boot 4 专用的 `mybatis-plus-spring-boot4-starter`。自 3.5.9 起，分页拦截器依赖需单独引入，因此新增同版本的 `mybatis-plus-jsqlparser`，以保留原有 `PaginationInnerInterceptor` 配置。
- 移除 MyBatis Spring Boot Starter。它与 MyBatis-Plus 功能重叠，且显式声明的 `3.0.3` 版本不适用于 Boot 4 集成；MyBatis-Plus Starter 已提供所需集成。
- MyBatis-Plus Generator：由 `RELEASE` 改为 `3.5.16`。`RELEASE` 是不可复现的浮动版本，现与 ORM 运行时版本对齐。
- Spring Framework/Security、Jackson、Tomcat、MySQL Driver 与测试库均由 Boot 4.0.3 BOM 管理，不再手动覆盖。Spring Security 7 移除了无参 `DaoAuthenticationProvider` 构造方法，认证管理器改为传入现有 `UserDetailsService`。Spring 7 也移除了 `MediaType.APPLICATION_JSON_UTF8_VALUE`，JSON 请求识别改为受支持的 `MediaType` 兼容性判断，并继续接受带字符集的 JSON。

## 前端

- Axios：`^1.6.4` 升级至 `1.20.0`。
- Element Plus：`^2.4.4` 升级至 `2.14.5`。
- Pinia：`^2.1.7` 升级至 `4.0.3`，项目继续使用受支持的 setup store 与 options store API。
- Vue：`^3.3.11` 升级至 `3.5.42`。Vue 3.5 使解构后的 `defineProps` 值具备响应性；当前代码不依赖旧行为。
- Vue Router：`^4.2.5` 升级至 `5.3.1`，现有的 `createRouter`、`createWebHistory`、导航守卫和 `addRoute` 调用仍受支持。
- Vite：`^5.0.10` 升级至 `8.3.0`；`@vitejs/plugin-vue`：`^4.5.2` 升级至 `6.0.8`。Vite 8 需要现代 Node.js 运行时，已在 Node 25.8.1 环境中验证。

## 验证范围

安装依赖后，应执行 Maven reactor 构建与 Vite 生产构建。后续失败应视为源码或环境迁移问题处理，而不应通过降级依赖来掩盖。

## 运行时兼容性改造

- 后端 JSON 处理改用 Spring Boot 4 的 Jackson 3 Mapper。旧 Jackson 2 的全局 Mapper 配置无法影响 MVC 的 Jackson 3 Converter。日期字段改为显式使用 `@JsonFormat` 声明接口格式，保持 `yyyy-MM-dd` 与 `yyyy-MM-dd HH:mm:ss` 契约。
- JSON 登录过滤器和全部自定义安全响应共用应用配置的 Mapper，不再临时创建 Mapper。
- 前端使用 Vue Router 的返回值式导航守卫，替代旧的 `next` 回调。动态菜单加载会传播失败，并在会话恢复失败时安全回到登录页。
- Axios 使用有效的请求透传拦截器，并在网络异常不存在 HTTP 响应时安全处理。Element Plus 分页和键盘事件改为当前 Vue 3 兼容绑定。
