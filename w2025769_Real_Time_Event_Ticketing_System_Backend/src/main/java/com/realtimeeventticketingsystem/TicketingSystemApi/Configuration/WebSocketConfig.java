package com.realtimeeventticketingsystem.TicketingSystemApi.Configuration;

import com.realtimeeventticketingsystem.TicketingSystemApi.logger.LogWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(logWebSocketHandler(), "/logs")
                .addInterceptors(new HttpSessionHandshakeInterceptor()) // optional
                .setAllowedOrigins("*"); // allow all origins
    }

    @Bean
    public WebSocketHandler logWebSocketHandler() {
        return new LogWebSocketHandler(); // Custom WebSocket handler
    }
}
