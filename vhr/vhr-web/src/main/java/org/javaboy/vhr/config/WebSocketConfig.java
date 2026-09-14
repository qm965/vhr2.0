package org.javaboy.vhr.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@Configuration @EnableWebSocketMessageBroker public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
 public void registerStompEndpoints(StompEndpointRegistry r){r.addEndpoint("/ws/ep").setAllowedOriginPatterns("http://127.0.0.1:*","http://localhost:*");}
 public void configureMessageBroker(MessageBrokerRegistry r){r.enableSimpleBroker("/queue");r.setApplicationDestinationPrefixes("/app");}
}
