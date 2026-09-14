package org.javaboy.vhr.controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.javaboy.vhr.framework.entity.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;import java.security.SecureRandom;
@RestController public class LoginController {
 private static final String CHARS="ABCDEFGHJKLMNPQRSTUVWXYZ23456789";private final SecureRandom random=new SecureRandom();
 @GetMapping("/login") public RespBean login(){return RespBean.error("尚未登录，请登录");}
 @GetMapping(value="/verifyCode",produces="image/svg+xml") public void code(HttpServletRequest req,HttpServletResponse resp)throws IOException{StringBuilder text=new StringBuilder();for(int i=0;i<4;i++)text.append(CHARS.charAt(random.nextInt(CHARS.length())));req.getSession().setAttribute("verify_code",text.toString());resp.setContentType("image/svg+xml;charset=UTF-8");resp.getWriter().write("<svg xmlns='http://www.w3.org/2000/svg' width='110' height='38'><rect width='100%' height='100%' fill='#f5f7fa'/><text x='10' y='27' font-family='monospace' font-weight='bold' font-size='24' letter-spacing='5'>"+text+"</text></svg>");}
}
