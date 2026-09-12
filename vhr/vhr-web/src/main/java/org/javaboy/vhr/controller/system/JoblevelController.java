package org.javaboy.vhr.controller.system;

import org.javaboy.vhr.framework.entity.RespBean;
import org.javaboy.vhr.framework.entity.RespPageBean;
import org.javaboy.vhr.system.entity.Joblevel;
import org.javaboy.vhr.system.service.IJoblevelService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/basic/joblevels")
public class JoblevelController {
    private final IJoblevelService joblevelService;

    public JoblevelController(IJoblevelService joblevelService) {
        this.joblevelService = joblevelService;
    }

    @GetMapping
    public RespPageBean getJoblevels(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return joblevelService.getJoblevelsByPage(page, size);
    }

    @PostMapping
    public RespBean addJoblevel(@RequestBody Joblevel joblevel) {
        return joblevelService.addJoblevel(joblevel);
    }

    @PutMapping
    public RespBean updateJoblevel(@RequestBody Joblevel joblevel) {
        return joblevelService.updateJoblevel(joblevel);
    }

    @DeleteMapping("/{id}")
    public RespBean deleteJoblevel(@PathVariable Integer id) {
        return joblevelService.deleteJoblevel(id);
    }
}
