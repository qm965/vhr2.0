package org.javaboy.vhr.controller.config;

import org.javaboy.vhr.framework.entity.Menu;
import org.javaboy.vhr.framework.entity.vo.MenuVO;
import org.javaboy.vhr.framework.service.IMenuService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/** Exact vhr 1.0 system-config menu endpoint: returns the enabled three-level menu tree. */
@RestController @RequestMapping("/system/config") public class SystemConfigController {
 private final IMenuService menus; public SystemConfigController(IMenuService menus){this.menus=menus;}
 @GetMapping("/menu") public List<MenuVO> getMenusByHrId(){
  Map<Integer,MenuVO> nodes=new LinkedHashMap<>(); for(Menu m:menus.list()){if(Boolean.TRUE.equals(m.getEnabled()))nodes.put(m.getId(),copy(m));}
  List<MenuVO> roots=new ArrayList<>(); for(MenuVO node:nodes.values()){MenuVO parent=nodes.get(node.getParentId());if(parent==null)roots.add(node);else {if(parent.getChildren()==null)parent.setChildren(new ArrayList<>());parent.getChildren().add(node);}} return roots;
 }
 private static MenuVO copy(Menu m){MenuVO x=new MenuVO();x.setId(m.getId());x.setName(m.getName());x.setUrl(m.getUrl());x.setPath(m.getPath());x.setComponent(m.getComponent());x.setIconCls(m.getIconCls());x.setParentId(m.getParentId());x.setEnabled(m.getEnabled());x.setKeepAlive(m.getKeepAlive());x.setRequireAuth(m.getRequireAuth());return x;}
}
