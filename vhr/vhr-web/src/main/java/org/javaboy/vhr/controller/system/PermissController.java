package org.javaboy.vhr.controller.system;
import org.javaboy.vhr.framework.entity.Menu;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.entity.Role;
import org.javaboy.vhr.framework.mapper.MenuMapper;
import org.javaboy.vhr.framework.service.IMenuService;
import org.javaboy.vhr.framework.service.IRoleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/system/basic/permiss") public class PermissController {
 private final IRoleService roles; private final IMenuService menus; private final MenuMapper mapper;
 public PermissController(IRoleService roles,IMenuService menus,MenuMapper mapper){this.roles=roles;this.menus=menus;this.mapper=mapper;}
 @GetMapping({"","/"}) public Object roles(){return roles.list();}
 @GetMapping("/menus") public Object menuTree(){return menus.list();}
 @GetMapping("/mids/{rid}") public Object menuIds(@PathVariable Integer rid){return mapper.getMenuIdsByRoleId(rid);}
 @PutMapping({"","/"}) @Transactional public RespBean update(@RequestParam Integer rid,@RequestParam(required=false) Integer[] mids){mapper.deleteMenusByRoleId(rid);return mids==null||mids.length==0||mapper.addMenus(rid,mids)==mids.length?RespBean.ok("更新成功"):RespBean.error("更新失败");}
 @PostMapping("/role") public RespBean addRole(@RequestBody Role role){if(role.getName()!=null&&!role.getName().startsWith("ROLE_"))role.setName("ROLE_"+role.getName());return roles.save(role)?RespBean.ok("添加成功",role):RespBean.error("添加失败");}
 @DeleteMapping("/role/{rid}") public RespBean deleteRole(@PathVariable Integer rid){return roles.removeById(rid)?RespBean.ok("删除成功"):RespBean.error("删除失败");}
}
