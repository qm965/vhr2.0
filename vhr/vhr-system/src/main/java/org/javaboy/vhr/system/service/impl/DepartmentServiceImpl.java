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
import java.util.Objects;

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
    public RespBean updateDepartment(Integer id, Department request) {
        if (request == null || request.getName() == null || request.getName().isBlank() || request.getParentId() == null) {
            return RespBean.error("部门名称和上级部门不能为空");
        }
        Department department = getById(id);
        if (department == null) {
            return RespBean.error("部门不存在");
        }
        boolean root = department.getParentId() == null || department.getParentId() == -1;
        if (root && !Objects.equals(department.getParentId(), request.getParentId())) {
            return RespBean.error("顶级部门不允许调整上级部门");
        }
        String name = request.getName().trim();
        if (root) {
            boolean duplicate = count(new LambdaQueryWrapper<Department>()
                    .eq(Department::getParentId, department.getParentId())
                    .eq(Department::getName, name)
                    .ne(Department::getId, id)) > 0;
            if (duplicate) {
                return RespBean.error("同一上级部门下不允许重名");
            }
            department.setName(name);
            return updateById(department) ? RespBean.ok("更新部门成功", department) : RespBean.error("更新部门失败");
        }
        Department targetParent = getById(request.getParentId());
        if (targetParent == null) {
            return RespBean.error("上级部门不存在");
        }
        if (Objects.equals(id, targetParent.getId())) {
            return RespBean.error("上级部门不能是自身");
        }
        String oldPath = department.getDepPath();
        if (oldPath != null && targetParent.getDepPath() != null && targetParent.getDepPath().startsWith(oldPath + ".")) {
            return RespBean.error("上级部门不能是当前部门的子孙部门");
        }
        boolean duplicate = count(new LambdaQueryWrapper<Department>()
                .eq(Department::getParentId, targetParent.getId())
                .eq(Department::getName, name)
                .ne(Department::getId, id)) > 0;
        if (duplicate) {
            return RespBean.error("同一上级部门下不允许重名");
        }

        Integer oldParentId = department.getParentId();
        boolean parentChanged = !Objects.equals(oldParentId, targetParent.getId());
        String newPath = targetParent.getDepPath() + "." + id;
        department.setName(name);
        if (parentChanged) {
            department.setParentId(targetParent.getId());
            department.setDepPath(newPath);
        }
        if (!updateById(department)) {
            return RespBean.error("更新部门失败");
        }
        if (parentChanged) {
            baseMapper.replaceDescendantPathPrefix(oldPath, newPath);
            Department oldParent = getById(oldParentId);
            if (oldParent != null && count(new LambdaQueryWrapper<Department>().eq(Department::getParentId, oldParent.getId())) == 0) {
                oldParent.setIsParent(false);
                updateById(oldParent);
            }
            if (!Boolean.TRUE.equals(targetParent.getIsParent())) {
                targetParent.setIsParent(true);
                updateById(targetParent);
            }
        }
        return RespBean.ok("更新部门成功", department);
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
