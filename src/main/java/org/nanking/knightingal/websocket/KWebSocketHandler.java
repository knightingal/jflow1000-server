package org.nanking.knightingal.websocket;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nanking.knightingal.service.WsMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class KWebSocketHandler extends TextWebSocketHandler {
    private static final Log log = LogFactory.getLog(KWebSocketHandler.class);

    @Autowired
    WsMsgService wsMsgService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("enter handleTextMessage");
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("enter afterConnectionClosed");
        wsMsgService.setSession(null);
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("enter afterConnectionEstablished");
        wsMsgService.setSession(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("enter handlePongMessage");
        super.handlePongMessage(session, message);
    }
}
