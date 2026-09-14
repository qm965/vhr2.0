package org.javaboy.vhr.controller.personnel;
import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.system.entity.Employeeec;
import org.javaboy.vhr.system.service.IEmployeeecService;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/personnel/ec") public class EmployeeEcController {
 private final IEmployeeecService records;public EmployeeEcController(IEmployeeecService records){this.records=records;}
 @GetMapping public Object list(){return records.list();}
 @PostMapping public RespBean add(@RequestBody Employeeec item){return records.save(item)?RespBean.ok("新增成功",item):RespBean.error("新增失败");}
 @PutMapping public RespBean update(@RequestBody Employeeec item){return records.updateById(item)?RespBean.ok("更新成功"):RespBean.error("更新失败");}
 @DeleteMapping("/{id}") public RespBean delete(@PathVariable Integer id){return records.removeById(id)?RespBean.ok("删除成功"):RespBean.error("删除失败");}
}
