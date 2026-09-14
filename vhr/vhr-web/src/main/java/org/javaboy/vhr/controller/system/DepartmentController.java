package org.javaboy.vhr.controller.system;

import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.system.entity.Department;
import org.javaboy.vhr.system.service.IDepartmentService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@RestController
@RequestMapping("/system/basic/departments")
public class DepartmentController {

    private final IDepartmentService departmentService;

    public DepartmentController(IDepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public RespBean getDepartmentTree() {
        List<Department> departments = departmentService.getDepartmentTree();
        return RespBean.ok("加载部门成功", departments);
    }

    @PostMapping
    public RespBean addChildDepartment(@RequestBody Department department) {
        return departmentService.addChildDepartment(department);
    }

    @PutMapping("/{id}")
    public RespBean updateDepartment(@PathVariable Integer id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public RespBean deleteDepartment(@PathVariable Integer id) {
        return departmentService.deleteDepartment(id);
    }
}
