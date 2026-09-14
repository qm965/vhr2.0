package org.javaboy.vhr.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.javaboy.vhr.framework.entity.Hr;
import org.javaboy.vhr.framework.service.IHrService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/chat") public class ChatController {
 private final IHrService hrs;public ChatController(IHrService hrs){this.hrs=hrs;}
 @GetMapping("/hrs") public Object users(Authentication a){Hr me=(Hr)a.getPrincipal();return hrs.list(new QueryWrapper<Hr>().ne("id",me.getId())).stream().peek(h->h.setPassword(null)).toList();}
}
