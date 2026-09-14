package org.javaboy.vhr.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.javaboy.vhr.framework.entity.Hr;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.mapper.HrMapper;
import org.javaboy.vhr.framework.service.IHrService;
import org.javaboy.vhr.framework.service.IRoleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** Operator and role maintenance API retained from vhr 1.0. */
@RestController @RequestMapping("/system/hr")
public class HrController {
 private final IHrService hrs; private final IRoleService roles; private final HrMapper mapper;
 public HrController(IHrService hrs, IRoleService roles, HrMapper mapper){this.hrs=hrs;this.roles=roles;this.mapper=mapper;}
 @GetMapping({"", "/"}) public List<Hr> list(String keywords){QueryWrapper<Hr> q=new QueryWrapper<>();q.like(keywords!=null&&!keywords.isBlank(),"name",keywords).or().like(keywords!=null&&!keywords.isBlank(),"username",keywords);List<Hr> result=hrs.list(keywords==null||keywords.isBlank()?null:q);result.forEach(h->{h.setPassword(null);h.setRoles(mapper.getHrRolesByHrId(h.getId()));});return result;}
 @PutMapping({"", "/"}) public RespBean update(@RequestBody Hr hr){hr.setPassword(null);return hrs.updateById(hr)?RespBean.ok("更新成功"):RespBean.error("更新失败");}
 @GetMapping("/roles") public Object allRoles(){return roles.list();}
 @PutMapping("/role") @Transactional public RespBean updateRoles(@RequestParam Integer hrid,@RequestParam(required=false) Integer[] rids){mapper.deleteRolesByHrId(hrid);return rids==null||rids.length==0||mapper.addRoles(hrid,rids)==rids.length?RespBean.ok("更新成功"):RespBean.error("更新失败");}
 @DeleteMapping("/{id}") public RespBean delete(@PathVariable Integer id){return hrs.removeById(id)?RespBean.ok("删除成功"):RespBean.error("删除失败");}
}
