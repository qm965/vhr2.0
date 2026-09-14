package org.javaboy.vhr.framework.mapper;

import org.javaboy.vhr.framework.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.javaboy.vhr.framework.entity.vo.MenuVO;
import org.javaboy.vhr.framework.entity.vo.MenuWithRole;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author javaboy
 * @since 2024-01-03
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<MenuVO> getMenusByHrId(Integer hrid);

    List<MenuWithRole> getAllMenusWithRole();

    @Select("select mid from menu_role where rid=#{rid}") List<Integer> getMenuIdsByRoleId(@Param("rid") Integer rid);
    @Delete("delete from menu_role where rid=#{rid}") int deleteMenusByRoleId(@Param("rid") Integer rid);
    @Insert({"<script>", "insert into menu_role(mid,rid) values", "<foreach collection='mids' item='mid' separator=','>(#{mid},#{rid})</foreach>", "</script>"}) int addMenus(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
