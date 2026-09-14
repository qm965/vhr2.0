package org.javaboy.vhr.controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import java.time.Instant;import java.util.Map;
@Controller public class WsController {private final SimpMessagingTemplate messages;public WsController(SimpMessagingTemplate messages){this.messages=messages;}@MessageMapping("/ws/chat") public void send(Authentication a,Map<String,String> input){String to=input.get("to"),content=input.get("content");if(to!=null&&content!=null&&!content.isBlank())messages.convertAndSendToUser(to,"/queue/chat",Map.of("from",a.getName(),"to",to,"content",content,"date",Instant.now().toString()));}}
