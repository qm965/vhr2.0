package org.javaboy.vhr.system.mapper;

import org.javaboy.vhr.system.entity.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author javaboy
 * @since 2024-01-10
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    @Select("select count(*) from employee where department_id = #{departmentId}")
    long countEmployeesByDepartmentId(Integer departmentId);

}
