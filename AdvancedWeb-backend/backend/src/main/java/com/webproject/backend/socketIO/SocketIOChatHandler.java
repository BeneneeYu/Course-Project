package com.webproject.backend.socketIO;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.webproject.backend.Response.Message;
import com.webproject.backend.entity.MessageInfo;
import com.webproject.backend.entity.User;
import com.webproject.backend.mapper.UserMapper;
import com.webproject.backend.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketIOChatHandler {

    private HistoryService historyService;

    @Autowired
    public SocketIOChatHandler(HistoryService historyService) {
        this.historyService = historyService;
    }

    @OnEvent(value = "receiveChat")
    public void receiveChat(SocketIOClient client, AckRequest ackRequest, MessageInfo messageInfo) {
        String messageType = messageInfo.getMessageType();
        if(!messageType.equals("Chat")) {
            client.sendEvent("updateChat", Message.newMessage("Wrong Type"));
        }else {
            pushPublicChat(messageInfo);
            historyService.pushChatHistory(messageInfo);
        }
    }

    public void broadcastChat(int userId, String username, String id) {
        if(SocketIOSession.CLIENT_MAP.size() == 0){
            return;
        }
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setUserId(userId);
        messageInfo.setUsername(username);
        messageInfo.setMessageType("Broadcast");
        messageInfo.setMessage(username + "joined in our game !");
        SocketIOSession.CLIENT_MAP.forEach((sessionId, client) -> {
            if(!sessionId.equals(id))
                client.sendEvent("updateChat", JSON.toJSONString(messageInfo));
        });
    }

    private void pushPublicChat(MessageInfo messageInfo) {
        SocketIOSession.CLIENT_MAP.forEach((sessionId, client) -> {
            client.sendEvent("updateChat", JSON.toJSONString(messageInfo));
        });
    }
}
