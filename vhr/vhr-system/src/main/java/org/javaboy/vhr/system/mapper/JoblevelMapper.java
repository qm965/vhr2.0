package org.javaboy.vhr.system.mapper;

import org.javaboy.vhr.system.entity.Joblevel;
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
public interface JoblevelMapper extends BaseMapper<Joblevel> {

    @Select("select count(*) from employee where job_level_id = #{joblevelId}")
    long countEmployeesByJoblevelId(Integer joblevelId);

}
