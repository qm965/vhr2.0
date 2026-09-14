package org.javaboy.vhr.framework.mapper;

import org.javaboy.vhr.framework.entity.Hr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.javaboy.vhr.framework.entity.Role;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author javaboy
 * @since 2024-01-03
 */
public interface HrMapper extends BaseMapper<Hr> {

    List<Role> getHrRolesByHrId(Integer hrid);

    @Delete("delete from hr_role where hr_id=#{hrid}")
    int deleteRolesByHrId(@Param("hrid") Integer hrid);

    @Insert({"<script>", "insert into hr_role(hr_id,rid) values",
            "<foreach collection='rids' item='rid' separator=','>(#{hrid},#{rid})</foreach>", "</script>"})
    int addRoles(@Param("hrid") Integer hrid, @Param("rids") Integer[] rids);
}
