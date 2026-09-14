package org.javaboy.vhr.controller.emp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.javaboy.vhr.employee.entity.*;
import org.javaboy.vhr.employee.mapper.NationMapper;
import org.javaboy.vhr.employee.mapper.PoliticsstatusMapper;
import org.javaboy.vhr.employee.service.EmployeeService;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.entity.RespPageBean;
import org.javaboy.vhr.system.service.IDepartmentService;
import org.javaboy.vhr.system.service.IJoblevelService;
import org.javaboy.vhr.system.service.IPositionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** vhr 1.0 employee-basic API, implemented on the vhr2 MyBatis-Plus modules. */
@RestController @RequestMapping("/employee/basic")
public class EmpBasicController {
 private final EmployeeService employees; private final NationMapper nations; private final PoliticsstatusMapper politics;
 private final IJoblevelService joblevels; private final IPositionService positions; private final IDepartmentService departments;
 public EmpBasicController(EmployeeService employees, NationMapper nations, PoliticsstatusMapper politics, IJoblevelService joblevels, IPositionService positions, IDepartmentService departments) { this.employees=employees; this.nations=nations; this.politics=politics; this.joblevels=joblevels; this.positions=positions; this.departments=departments; }
 @GetMapping({"", "/"}) public RespPageBean getEmployeeByPage(@RequestParam(defaultValue="1") Long page, @RequestParam(defaultValue="10") Long size, String name) { QueryWrapper<Employee> q=new QueryWrapper<>(); q.like(name!=null&&!name.isBlank(), "name", name).orderByAsc("id"); Page<Employee> result=employees.page(Page.of(page,size),q); return new RespPageBean(result.getTotal(),result.getRecords()); }
 @PostMapping({"", "/"}) public RespBean addEmp(@RequestBody Employee employee) { return employees.save(employee) ? RespBean.ok("添加成功",employee) : RespBean.error("添加失败"); }
 @PutMapping({"", "/"}) public RespBean updateEmp(@RequestBody Employee employee) { return employees.updateById(employee) ? RespBean.ok("更新成功") : RespBean.error("更新失败"); }
 @DeleteMapping("/{id}") public RespBean deleteEmp(@PathVariable Integer id) { return employees.removeById(id) ? RespBean.ok("删除成功") : RespBean.error("删除失败"); }
 @GetMapping("/nations") public List<Nation> nations(){return nations.selectList(null);} @GetMapping("/politicsstatus") public List<Politicsstatus> politics(){return politics.selectList(null);}
 @GetMapping("/joblevels") public Object joblevels(){return joblevels.list();} @GetMapping("/positions") public Object positions(){return positions.list();} @GetMapping("/deps") public Object deps(){return departments.getDepartmentTree();}
 @GetMapping("/maxWorkID") public RespBean maxWorkId(){ Employee e=employees.getOne(new QueryWrapper<Employee>().orderByDesc("work_id").last("limit 1")); int next=e==null?1:Integer.parseInt(e.workID)+1; return RespBean.ok("查询成功",String.format("%08d",next)); }
}
