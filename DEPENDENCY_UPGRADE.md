# vhr2.0 dependency upgrade

This update moves each direct third-party dependency to the newest stable release
verified on 2026-09-11. Frontend versions are exact rather than ranges so a fresh
install is repeatable; the generated `package-lock.json` records transitive
dependencies.

## Backend

- Spring Boot: `3.2.1` to `4.0.3`. Spring Boot 4 still runs on Java 17, which
  matches this project. Its servlet starter is now `spring-boot-starter-webmvc`;
  the old `spring-boot-starter-web` is deprecated.
- MyBatis-Plus: `3.5.5` to `3.5.16`. The obsolete generic
  `mybatis-plus-boot-starter` is replaced by the Spring Boot 4-specific
  `mybatis-plus-spring-boot4-starter`. Since 3.5.9, the pagination interceptor
  is supplied separately, so `mybatis-plus-jsqlparser` is now declared at the
  same version to retain the existing `PaginationInnerInterceptor` setup.
- MyBatis Spring Boot Starter: removed. It overlapped with MyBatis-Plus and its
  explicit `3.0.3` release is not the Boot 4 integration. The MyBatis-Plus
  starter provides the required MyBatis integration.
- MyBatis-Plus Generator: `RELEASE` to `3.5.16`. `RELEASE` is a moving,
  non-reproducible Maven version; it is now aligned with the ORM runtime.
- Spring-managed dependencies (Spring Framework/Security, Jackson, Tomcat,
  MySQL driver and test libraries) move with the Boot 4.0.3 BOM instead of
  being manually overridden. Spring Security 7 removes the no-argument
  `DaoAuthenticationProvider` constructor, so the existing authentication
  manager now passes its `UserDetailsService` to the constructor. Spring 7 also
  removes `MediaType.APPLICATION_JSON_UTF8_VALUE`; JSON request detection now
  uses the supported `MediaType` compatibility check and continues accepting
  JSON with a charset.

## Frontend

- Axios: `^1.6.4` to `1.20.0`.
- Element Plus: `^2.4.4` to `2.14.5`.
- Pinia: `^2.1.7` to `4.0.3`. The app uses the supported setup-store and
  options-store APIs.
- Vue: `^3.3.11` to `3.5.42`. Vue 3.5 makes destructured `defineProps` values
  reactive; this source does not rely on the old behavior.
- Vue Router: `^4.2.5` to `5.3.1`. Existing `createRouter`,
  `createWebHistory`, navigation guards and `addRoute` calls remain in use.
- Vite: `^5.0.10` to `8.3.0`, and `@vitejs/plugin-vue`: `^4.5.2` to `6.0.8`.
  Vite 8 requires a modern Node.js runtime; the checked environment uses Node
  25.8.1.

## Validation scope

Run the Maven reactor build and the Vite production build after dependencies are
installed. Any remaining failures should be treated as source or environment
migrations, not silently resolved with version downgrades.

## Runtime compatibility rewrite

- Backend JSON processing now uses Spring Boot 4's Jackson 3 mapper. The prior
  Jackson 2 global mapper configuration did not configure MVC's Jackson 3
  converter. Date fields now declare their API formats explicitly with
  `@JsonFormat`, preserving `yyyy-MM-dd` and `yyyy-MM-dd HH:mm:ss` contracts.
- The JSON login filter and every custom security response share the configured
  application mapper rather than creating ad-hoc mappers.
- The frontend uses Vue Router's return-value navigation guards instead of the
  legacy `next` callback. Dynamic menu loading now propagates failures and
  safely returns users to login when a session cannot be restored.
- Axios now has a valid pass-through request interceptor and handles failures
  without assuming a network error has an HTTP response. Element Plus paging and
  keyboard events use current Vue 3-compatible bindings.
