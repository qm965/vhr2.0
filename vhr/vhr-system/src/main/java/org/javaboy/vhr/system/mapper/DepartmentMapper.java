package org.javaboy.vhr.system.mapper;

import org.javaboy.vhr.system.entity.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update("update department set dep_path = concat(#{newPrefix}, substring(dep_path, char_length(#{oldPrefix}) + 1)) where dep_path like concat(#{oldPrefix}, '.%')")
    int replaceDescendantPathPrefix(String oldPrefix, String newPrefix);

}
