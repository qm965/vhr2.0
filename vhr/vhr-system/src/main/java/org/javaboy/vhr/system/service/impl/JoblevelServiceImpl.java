package org.javaboy.vhr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.entity.RespPageBean;
import org.javaboy.vhr.system.entity.Joblevel;
import org.javaboy.vhr.system.mapper.JoblevelMapper;
import org.javaboy.vhr.system.service.IJoblevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author javaboy
 * @since 2024-01-10
 */
@Service
public class JoblevelServiceImpl extends ServiceImpl<JoblevelMapper, Joblevel> implements IJoblevelService {

    private static final Set<String> TITLE_LEVELS = Set.of("正高级", "副高级", "中级", "初级", "员级");

    @Override
    public RespPageBean getJoblevelsByPage(Integer page, Integer size) {
        Page<Joblevel> result = page(new Page<>(page, size), new LambdaQueryWrapper<Joblevel>().orderByDesc(Joblevel::getId));
        return new RespPageBean(result.getTotal(), result.getRecords());
    }

    @Override
    public RespBean addJoblevel(Joblevel joblevel) {
        String error = validate(joblevel, null);
        if (error != null) return RespBean.error(error);
        joblevel.setName(joblevel.getName().trim());
        joblevel.setEnabled(joblevel.getEnabled() == null || joblevel.getEnabled());
        joblevel.setCreateDate(LocalDateTime.now());
        return save(joblevel) ? RespBean.ok("添加职称成功", joblevel) : RespBean.error("添加职称失败");
    }

    @Override
    public RespBean updateJoblevel(Joblevel joblevel) {
        if (joblevel == null || joblevel.getId() == null || getById(joblevel.getId()) == null) return RespBean.error("职称不存在");
        String error = validate(joblevel, joblevel.getId());
        if (error != null) return RespBean.error(error);
        joblevel.setName(joblevel.getName().trim());
        return updateById(joblevel) ? RespBean.ok("更新职称成功") : RespBean.error("更新职称失败");
    }

    @Override
    public RespBean deleteJoblevel(Integer id) {
        if (getById(id) == null) return RespBean.error("职称不存在");
        if (baseMapper.countEmployeesByJoblevelId(id) > 0) return RespBean.error("该职称下存在员工，无法删除");
        return removeById(id) ? RespBean.ok("删除职称成功") : RespBean.error("删除职称失败");
    }

    private String validate(Joblevel joblevel, Integer excludedId) {
        if (joblevel == null || joblevel.getName() == null || joblevel.getName().isBlank()) return "职称名称不能为空";
        if (joblevel.getTitleLevel() == null || !TITLE_LEVELS.contains(joblevel.getTitleLevel())) return "职称级别不合法";
        LambdaQueryWrapper<Joblevel> query = new LambdaQueryWrapper<Joblevel>().eq(Joblevel::getName, joblevel.getName().trim());
        if (excludedId != null) query.ne(Joblevel::getId, excludedId);
        return count(query) > 0 ? "职称名称重复" : null;
    }

}
