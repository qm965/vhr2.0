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
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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
 @GetMapping("/export") public void export(HttpServletResponse response) throws IOException {response.setContentType("text/csv;charset=UTF-8");response.setHeader("Content-Disposition","attachment; filename=employees.csv");try(Writer out=new OutputStreamWriter(response.getOutputStream(),StandardCharsets.UTF_8)){out.write("\uFEFF工号,姓名,性别,电话,邮箱,入职日期,状态\n");for(Employee e:employees.list()){out.write(csv(e.workID)+","+csv(e.name)+","+csv(e.gender)+","+csv(e.phone)+","+csv(e.email)+","+csv(e.beginDate==null?"":e.beginDate.toString())+","+csv(e.workState)+"\n");}}}
 @PostMapping("/import") public RespBean importCsv(@RequestParam MultipartFile file) {if(file.isEmpty())return RespBean.error("请选择 CSV 文件");int count=0;try(BufferedReader in=new BufferedReader(new InputStreamReader(file.getInputStream(),StandardCharsets.UTF_8))){String line;boolean first=true;while((line=in.readLine())!=null){if(first){first=false;continue;}String[] c=line.replace("\uFEFF","").split(",",-1);if(c.length<2||c[1].isBlank())continue;Employee e=new Employee();e.workID=c[0].trim();e.name=c[1].trim();e.gender=c.length>2?c[2].trim():null;e.phone=c.length>3?c[3].trim():null;e.email=c.length>4?c[4].trim():null;e.beginDate=c.length>5&&!c[5].isBlank()?LocalDate.parse(c[5].trim()):null;e.workState=c.length>6?c[6].trim():"在职";employees.save(e);count++;}}catch(Exception e){return RespBean.error("导入失败：请使用导出模板的 CSV 格式");}return RespBean.ok("成功导入 "+count+" 名员工");}
 private static String csv(String s){return s==null?"":"\""+s.replace("\"","\"\"")+"\"";}
}
