package org.nanking.knightingal.service.impl;

import org.nanking.knightingal.service.WsMsgService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class WsMsgServiceImpl implements WsMsgService {

    WebSocketSession session;

    @Override
    public void sendWsMsg(String msg) {
        TextMessage textMessage = new TextMessage(msg);
        if (session != null) {
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setSession(WebSocketSession session) {
        this.session = session;
    }
}
