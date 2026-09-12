package org.javaboy.vhr.system.mapper;

import org.javaboy.vhr.system.entity.Position;
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
public interface PositionMapper extends BaseMapper<Position> {

    @Select("select count(*) from employee where pos_id = #{positionId}")
    long countEmployeesByPositionId(Integer positionId);

}
