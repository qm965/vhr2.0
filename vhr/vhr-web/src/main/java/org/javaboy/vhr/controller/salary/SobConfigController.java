package org.javaboy.vhr.controller.salary;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.salary.mapper.EmpSalaryMapper;
import org.javaboy.vhr.salary.service.SalaryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/salary/sobcfg") public class SobConfigController {
 private final EmpSalaryMapper assignments; private final SalaryService salaries;
 public SobConfigController(EmpSalaryMapper assignments,SalaryService salaries){this.assignments=assignments;this.salaries=salaries;}
 @GetMapping({"","/"}) public Object list(){return assignments.listAssignments();}
 @GetMapping("/salaries") public Object salaries(){return salaries.list();}
 @PutMapping({"","/"}) @Transactional public RespBean assign(@RequestParam Integer eid,@RequestParam(required=false) Integer sid){assignments.clear(eid);return sid==null?RespBean.ok("已取消账套"):assignments.assign(eid,sid)==1?RespBean.ok("设置成功"):RespBean.error("设置失败");}
}
