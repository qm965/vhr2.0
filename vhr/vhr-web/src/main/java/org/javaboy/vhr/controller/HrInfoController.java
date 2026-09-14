package org.javaboy.vhr.controller;
import org.javaboy.vhr.framework.entity.Hr;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.service.IHrService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController public class HrInfoController {
 private final IHrService hrs; private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
 public HrInfoController(IHrService hrs){this.hrs=hrs;}
 @GetMapping("/hr/info") public Hr current(Authentication a){Hr hr=(Hr)a.getPrincipal();hr.setPassword(null);return hr;}
 @PutMapping("/hr/info") public RespBean update(@RequestBody Hr input,Authentication a){Hr current=(Hr)a.getPrincipal();input.setId(current.getId());input.setUsername(current.getUsername());input.setPassword(null);return hrs.updateById(input)?RespBean.ok("更新成功"):RespBean.error("更新失败");}
 @PutMapping("/hr/pass") public RespBean password(@RequestBody Map<String,String> info,Authentication a){Hr current=(Hr)a.getPrincipal();Hr stored=hrs.getById(current.getId());String old=info.get("oldpass"),next=info.get("pass");String saved=stored.getPassword();boolean match=saved.startsWith("{noop}")?saved.substring(6).equals(old):encoder.matches(old,saved);if(old==null||next==null||next.length()<6||!match)return RespBean.error("旧密码错误或新密码不符合要求");stored.setPassword(encoder.encode(next));return hrs.updateById(stored)?RespBean.ok("密码修改成功，请重新登录"):RespBean.error("更新失败");}
}
