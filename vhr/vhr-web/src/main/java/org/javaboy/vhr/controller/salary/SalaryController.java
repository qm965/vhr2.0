package org.javaboy.vhr.controller.salary;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.salary.entity.Salary;
import org.javaboy.vhr.salary.service.SalaryService;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/salary/sob") public class SalaryController {
 private final SalaryService salaries; public SalaryController(SalaryService salaries){this.salaries=salaries;}
 @GetMapping({"", "/"}) public Object getAllSalaries(){return salaries.list();}
 @PostMapping({"", "/"}) public RespBean addSalary(@RequestBody Salary salary){return salaries.save(salary)?RespBean.ok("添加成功",salary):RespBean.error("添加失败");}
 @PutMapping({"", "/"}) public RespBean updateSalary(@RequestBody Salary salary){return salaries.updateById(salary)?RespBean.ok("更新成功"):RespBean.error("更新失败");}
 @DeleteMapping("/{id}") public RespBean deleteSalary(@PathVariable Integer id){return salaries.removeById(id)?RespBean.ok("删除成功"):RespBean.error("删除失败");}
}
