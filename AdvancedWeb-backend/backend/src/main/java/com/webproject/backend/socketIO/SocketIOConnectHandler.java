package com.webproject.backend.socketIO;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webproject.backend.Response.Message;
import com.webproject.backend.entity.MessageInfo;
import com.webproject.backend.entity.User;
import com.webproject.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * @description
 * socket.io事件处理
 * @onConnect 处理连接
 * @diconnect 关闭连接
 * sendEvent 向前端发送事件
 * @OnEvent 处理事件
 */

@Component
public class SocketIOConnectHandler {

    private SocketIOServer server;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SocketIOConnectHandler(SocketIOServer server,UserMapper mapper,PasswordEncoder passwordEncoder){
        this.server = server;
        this.userMapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        System.out.println("client connected : " + client.getSessionId());
        client.sendEvent("connected", Message.newMessage("success"));
        SocketIOSession.CLIENT_MAP.putIfAbsent(client.getSessionId().toString(),client);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        System.out.println("client disconnected : " + client.getSessionId());
        String id = client.getSessionId().toString();
        SocketIOSession.CLIENT_MAP.remove(id);
        if(SocketIOSession.USER_MAP.containsKey(id)) {
            int userId = SocketIOSession.USER_MAP.get(id);
            SocketIOSession.USER_POSITION.remove(userId);
            SocketIOSession.USER_MAP.remove(id);broadcastLogout(userId);
        }
        client.sendEvent("disconnected",Message.newMessage("success"));

    }

    private void broadcastLogout(int userId){
        MessageInfo messageInfo = new MessageInfo();
        String username = userMapper.getUser(userId).getUsername();
        messageInfo.setUserId(userId);
        messageInfo.setUsername(username);
        messageInfo.setMessageType("Logout");
        messageInfo.setMessage("Bye");
        SocketIOSession.CLIENT_MAP.forEach((sessionId, client) -> {
            client.sendEvent("logout", JSON.toJSONString(messageInfo));
        });
    }

}
