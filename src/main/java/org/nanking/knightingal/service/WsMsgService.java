package org.nanking.knightingal.service;

import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;

public interface WsMsgService {

    void sendWsMsg(String msg);

    void setSession(WebSocketSession session);
}
