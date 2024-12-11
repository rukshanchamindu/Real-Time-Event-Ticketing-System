package com.realtimeeventticketingsystem.TicketingSystemApi.logger;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

public class LogWebSocketHandler extends TextWebSocketHandler {

    // Called when a new WebSocket connection is established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LoggerService.addSession(session);  // Add this session to the LoggerService
        //session.sendMessage(new TextMessage("Connected to WebSocket server"));
    }

    // Called when the WebSocket connection is closed
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        LoggerService.removeSession(session);  // Remove this session from the LoggerService
    }

    // Called when a text message is received from the client (this can be extended if needed)
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming message from WebSocket client (optional)
    }
}
