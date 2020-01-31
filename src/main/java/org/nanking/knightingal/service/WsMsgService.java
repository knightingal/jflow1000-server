package org.nanking.knightingal.service;

import org.springframework.web.socket.WebSocketSession;

/**
 * @author Knightingal
 */
public interface WsMsgService {

    void sendWsMsg(String msg);

    void setSession(WebSocketSession session);
}
