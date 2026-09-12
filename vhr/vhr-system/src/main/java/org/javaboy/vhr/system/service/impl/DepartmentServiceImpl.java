package org.javaboy.vhr.system.service.impl;

import org.javaboy.vhr.system.entity.Department;
import org.javaboy.vhr.system.mapper.DepartmentMapper;
import org.javaboy.vhr.system.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.javaboy.vhr.framework.entity.RespBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2024-01-10
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Override
    public List<Department> getDepartmentTree() {
        List<Department> departments = list(new LambdaQueryWrapper<Department>().orderByAsc(Department::getId));
        Map<Integer, Department> byId = new HashMap<>();
        List<Department> roots = new ArrayList<>();
        for (Department department : departments) {
            department.setChildren(new ArrayList<>());
            byId.put(department.getId(), department);
        }
        for (Department department : departments) {
            Department parent = byId.get(department.getParentId());
            if (parent == null) {
                roots.add(department);
            } else {
                parent.getChildren().add(department);
            }
        }
        return roots;
    }

    @Override
    @Transactional
    public RespBean addChildDepartment(Department department) {
        if (department == null || department.getName() == null || department.getName().isBlank() || department.getParentId() == null) {
            return RespBean.error("部门名称和上级部门不能为空");
        }
        Department parent = getById(department.getParentId());
        if (parent == null) {
            return RespBean.error("上级部门不存在");
        }
        String name = department.getName().trim();
        boolean duplicate = count(new LambdaQueryWrapper<Department>()
                .eq(Department::getParentId, parent.getId())
                .eq(Department::getName, name)) > 0;
        if (duplicate) {
            return RespBean.error("同一上级部门下不允许重名");
        }
        department.setName(name);
        department.setEnabled(department.getEnabled() == null || department.getEnabled());
        department.setIsParent(false);
        department.setDepPath(null);
        if (!save(department)) {
            return RespBean.error("添加部门失败");
        }
        department.setDepPath(parent.getDepPath() + "." + department.getId());
        updateById(department);
        if (!Boolean.TRUE.equals(parent.getIsParent())) {
            parent.setIsParent(true);
            updateById(parent);
        }
        department.setChildren(new ArrayList<>());
        return RespBean.ok("添加部门成功", department);
    }

    @Override
    @Transactional
    public RespBean deleteDepartment(Integer id) {
        Department department = getById(id);
        if (department == null) {
            return RespBean.error("部门不存在");
        }
        if (count(new LambdaQueryWrapper<Department>().eq(Department::getParentId, id)) > 0) {
            return RespBean.error("该部门下存在子部门，无法删除");
        }
        if (baseMapper.countEmployeesByDepartmentId(id) > 0) {
            return RespBean.error("该部门下存在员工，无法删除");
        }
        if (!removeById(id)) {
            return RespBean.error("删除部门失败");
        }
        Department parent = getById(department.getParentId());
        if (parent != null && count(new LambdaQueryWrapper<Department>().eq(Department::getParentId, parent.getId())) == 0) {
            parent.setIsParent(false);
            updateById(parent);
        }
        return RespBean.ok("删除部门成功");
    }

}
