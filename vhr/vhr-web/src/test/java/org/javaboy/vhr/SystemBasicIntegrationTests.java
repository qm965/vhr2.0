package org.javaboy.vhr;

import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.system.entity.Department;
import org.javaboy.vhr.system.entity.Position;
import org.javaboy.vhr.system.service.IDepartmentService;
import org.javaboy.vhr.system.service.IPositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** 使用当前 vhr2024 开发测试库验证基础信息和登录权限链路。 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SystemBasicIntegrationTests {
    private static final String AUTOMATION_USERNAME = "automation-test";
    private static final String AUTOMATION_PASSWORD = "automation-test-password";
    private static final String LEGACY_USERNAME = "automation-legacy-test";
    private static final String LEGACY_PASSWORD = "legacy-test-password";

    @Autowired IDepartmentService departmentService;
    @Autowired IPositionService positionService;
    @Autowired JdbcTemplate jdbcTemplate;
    @LocalServerPort int port;

    @BeforeEach
    void ensureAutomationAdministrator() {
        Integer roleId = jdbcTemplate.query("select id from role where name = 'ROLE_admin' limit 1", rs -> rs.next() ? rs.getInt(1) : null);
        if (roleId == null) {
            jdbcTemplate.update("insert into role(name, name_zh) values ('ROLE_admin', '自动化测试管理员')");
            roleId = jdbcTemplate.queryForObject("select id from role where name = 'ROLE_admin' order by id desc limit 1", Integer.class);
        }
        Integer hrId = jdbcTemplate.query("select id from hr where username = ? limit 1", ps -> ps.setString(1, AUTOMATION_USERNAME), rs -> rs.next() ? rs.getInt(1) : null);
        String encodedPassword = new BCryptPasswordEncoder().encode(AUTOMATION_PASSWORD);
        if (hrId == null) {
            jdbcTemplate.update("insert into hr(name, enabled, username, password) values (?, true, ?, ?)", "自动化测试账号", AUTOMATION_USERNAME, encodedPassword);
            hrId = jdbcTemplate.queryForObject("select id from hr where username = ?", Integer.class, AUTOMATION_USERNAME);
        } else {
            jdbcTemplate.update("update hr set enabled = true, password = ? where id = ?", encodedPassword, hrId);
        }
        jdbcTemplate.update("delete from hr_role where hr_id = ?", hrId);
        jdbcTemplate.update("insert into hr_role(hr_id, rid) values (?, ?)", hrId, roleId);
        Integer legacyHrId = jdbcTemplate.query("select id from hr where username = ? limit 1", ps -> ps.setString(1, LEGACY_USERNAME), rs -> rs.next() ? rs.getInt(1) : null);
        if (legacyHrId == null) {
            jdbcTemplate.update("insert into hr(name, enabled, username, password) values (?, true, ?, ?)", "旧密码格式测试账号", LEGACY_USERNAME, "{noop}" + LEGACY_PASSWORD);
        } else {
            jdbcTemplate.update("update hr set enabled = true, password = ? where id = ?", "{noop}" + LEGACY_PASSWORD, legacyHrId);
        }
    }

    @Test
    void canAddDepartmentAndRejectDuplicatePosition() {
        String stamp = String.valueOf(System.currentTimeMillis());
        Department parent = departmentService.getDepartmentTree().get(0);
        Department child = new Department();
        child.setName("自动化部门-" + stamp);
        child.setParentId(parent.getId());
        RespBean department = departmentService.addChildDepartment(child);
        assertEquals(200, department.getStatus());

        Position position = new Position();
        position.setName("自动化职位-" + stamp);
        assertEquals(200, positionService.addPosition(position).getStatus());
        Position duplicate = new Position();
        duplicate.setName(position.getName());
        assertEquals(500, positionService.addPosition(duplicate).getStatus());
    }

    @Test
    void canRenameAndMoveDepartmentSubtreeWithValidation() {
        String stamp = String.valueOf(System.currentTimeMillis());
        Department root = departmentService.getDepartmentTree().get(0);
        Department movable = addChild(root.getId(), "可移动部门-" + stamp);
        Department child = addChild(movable.getId(), "可移动子部门-" + stamp);
        Department target = addChild(root.getId(), "目标部门-" + stamp);

        Department moveRequest = new Department();
        moveRequest.setName(movable.getName());
        moveRequest.setParentId(target.getId());
        assertEquals(200, departmentService.updateDepartment(movable.getId(), moveRequest).getStatus());
        Department moved = departmentService.getById(movable.getId());
        Department movedChild = departmentService.getById(child.getId());
        assertEquals(target.getId(), moved.getParentId());
        assertEquals(target.getDepPath() + "." + moved.getId(), moved.getDepPath());
        assertEquals(moved.getDepPath() + "." + movedChild.getId(), movedChild.getDepPath());

        Department renameRequest = new Department();
        renameRequest.setName("已改名部门-" + stamp);
        renameRequest.setParentId(target.getId());
        assertEquals(200, departmentService.updateDepartment(moved.getId(), renameRequest).getStatus());
        assertEquals(moved.getDepPath(), departmentService.getById(moved.getId()).getDepPath());

        Department duplicate = addChild(root.getId(), "重名部门-" + stamp);
        addChild(target.getId(), duplicate.getName());
        Department duplicateMoveRequest = new Department();
        duplicateMoveRequest.setName(duplicate.getName());
        duplicateMoveRequest.setParentId(target.getId());
        assertEquals(500, departmentService.updateDepartment(duplicate.getId(), duplicateMoveRequest).getStatus());

        Department invalidMoveRequest = new Department();
        invalidMoveRequest.setName(target.getName());
        invalidMoveRequest.setParentId(child.getId());
        assertEquals(500, departmentService.updateDepartment(target.getId(), invalidMoveRequest).getStatus());

        Department rootMoveRequest = new Department();
        rootMoveRequest.setName(root.getName());
        rootMoveRequest.setParentId(target.getId());
        assertEquals(500, departmentService.updateDepartment(root.getId(), rootMoveRequest).getStatus());
    }

    @Test
    void httpEndpointsRequireLoginAndAcceptAdministratorSession() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        assertEquals(401, client.send(HttpRequest.newBuilder(uri("/system/basic/departments")).GET().build(), HttpResponse.BodyHandlers.ofString()).statusCode());
        HttpResponse<String> login = client.send(HttpRequest.newBuilder(uri("/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"" + AUTOMATION_USERNAME + "\",\"password\":\"" + AUTOMATION_PASSWORD + "\"}"))
                .build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, login.statusCode());
        String cookie = login.headers().firstValue("set-cookie").orElseThrow().split(";", 2)[0];
        assertEquals(200, client.send(HttpRequest.newBuilder(uri("/system/basic/departments")).header("Cookie", cookie).GET().build(), HttpResponse.BodyHandlers.ofString()).statusCode());
        HttpResponse<String> legacyLogin = client.send(HttpRequest.newBuilder(uri("/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"" + LEGACY_USERNAME + "\",\"password\":\"" + LEGACY_PASSWORD + "\"}"))
                .build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, legacyLogin.statusCode());
    }

    private URI uri(String path) {
        return URI.create("http://127.0.0.1:" + port + path);
    }

    private Department addChild(Integer parentId, String name) {
        Department department = new Department();
        department.setParentId(parentId);
        department.setName(name);
        RespBean response = departmentService.addChildDepartment(department);
        assertEquals(200, response.getStatus());
        return (Department) response.getData();
    }
}
