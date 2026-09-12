package org.javaboy.vhr.system.service;

import org.javaboy.vhr.system.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import org.javaboy.vhr.framework.entity.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author javaboy
 * @since 2024-01-10
 */
public interface IDepartmentService extends IService<Department> {

    List<Department> getDepartmentTree();

    RespBean addChildDepartment(Department department);

    RespBean deleteDepartment(Integer id);

}
