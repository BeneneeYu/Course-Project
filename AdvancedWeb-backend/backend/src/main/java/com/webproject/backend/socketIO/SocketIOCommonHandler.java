package com.webproject.backend.socketIO;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.webproject.backend.Response.Message;
import com.webproject.backend.entity.ChessMessage;
import com.webproject.backend.entity.MessageInfo;
import com.webproject.backend.mapper.UserMapper;
import com.webproject.backend.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SocketIOCommonHandler {

    private SocketIOChatHandler socketIOChatHandler;
    private UserMapper userMapper;
    private HistoryService historyService;

    @Autowired
    public SocketIOCommonHandler(SocketIOChatHandler socketIOChatHandler, UserMapper userMapper, HistoryService historyService) {
        this.socketIOChatHandler = socketIOChatHandler;
        this.userMapper = userMapper;
        this.historyService = historyService;
    }

    /**
     * 新用户加入，需要向新用户发送其他用户的位置，棋子的位置；
     * 需要系统公告新用户加入
     * @param client
     * @param ackRequest
     * @param messageInfo
     */
    @OnEvent(value = "joinIn")
    public void joinIn(SocketIOClient client, AckRequest ackRequest, MessageInfo messageInfo) {
        if(!messageInfo.getMessageType().equals("JoinIn")) {
            client.sendEvent("joinIn", Message.newMessage("Wrong Type"));
        }else {
            sendUsersPosition(client.getSessionId().toString());
            sendChessPosition(client.getSessionId().toString());
            socketIOChatHandler.broadcastChat(messageInfo.getUserId(), messageInfo.getUsername(), client.getSessionId().toString());
            SocketIOSession.USER_MAP.put(client.getSessionId().toString(), messageInfo.getUserId());
            SocketIOSession.USER_POSITION.put(messageInfo.getUserId(), messageInfo.getMessage());
        }
    }

    /**
     * 将其他玩家的位置信息同步给新加入的玩家
     * @param sessionId
     */
    private void sendUsersPosition(String sessionId) {
        SocketIOClient client = SocketIOSession.CLIENT_MAP.get(sessionId);
        SocketIOSession.USER_POSITION.forEach((userId, position) -> {
            String username = userMapper.getUser(userId).getUsername();
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setUserId(userId);
            messageInfo.setUsername(username);
            messageInfo.setMessageType("UserPosition");
            messageInfo.setMessage(position);
            client.sendEvent("usersPosition", JSON.toJSONString(messageInfo));
        });
    }

    /**
     * 将棋子的位置同步给新玩家
     * @param sessionId
     */
    private void sendChessPosition(String sessionId) {
        SocketIOClient client = SocketIOSession.CLIENT_MAP.get(sessionId);
        SocketIOSession.CHESS_POSITION.forEach((id, position) -> {
            ChessMessage chessMessage = new ChessMessage();
            chessMessage.setId(id);
            chessMessage.setPosition(position);
            chessMessage.setLocation(SocketIOSession.CHESS_LOCATION.get(id));
            client.sendEvent("chessPosition", JSON.toJSONString(chessMessage));
        });
    }

    /**
     * 更新用户的位置
     * @param client
     * @param ackRequest
     * @param messageInfo
     */
    @OnEvent(value = "updatePosition")
    public void updatePosition(SocketIOClient client, AckRequest ackRequest, MessageInfo messageInfo){
        if(!messageInfo.getMessageType().equals("updatePosition")){
            client.sendEvent("joinIn", Message.newMessage("Wrong Type"));
        }else{
            SocketIOSession.USER_POSITION.put(messageInfo.getUserId(), messageInfo.getMessage());
            MessageInfo message = new MessageInfo();
            message.setUserId(messageInfo.getUserId());
            message.setUsername(messageInfo.getUsername());
            message.setMessageType("movePosition");
            message.setMessage(messageInfo.getMessage());
            broadcastUserPosition(client.getSessionId().toString(), message);
        }
    }

    /**
     * 将用户更新的位置
     * @param id
     * @param messageInfo
     */
    private void broadcastUserPosition(String id, MessageInfo messageInfo){
        SocketIOSession.CLIENT_MAP.forEach((sessionId,client) -> {
            if(!sessionId.equals(id)){
                client.sendEvent("movePosition", JSON.toJSONString(messageInfo));
            }
        });
    }

    @OnEvent(value = "updateDisk")
    public void updateDisk(SocketIOClient client, AckRequest ackRequest, ChessMessage chessMessage){
        SocketIOSession.CHESS_POSITION.put(chessMessage.getId(),chessMessage.getPosition());
        SocketIOSession.CHESS_LOCATION.put(chessMessage.getId(),chessMessage.getLocation());
        broadcastDiskPosition(client.getSessionId().toString(), chessMessage);
    }

    private void broadcastDiskPosition(String id, ChessMessage chessMessage){
        SocketIOSession.CLIENT_MAP.forEach((sessionId,client) -> {
            if(!sessionId.equals(id)){
                client.sendEvent("moveDisk",JSON.toJSONString(chessMessage));
            }
        });
    }

    @OnEvent(value = "success")
    public void success(SocketIOClient socketIOClient, AckRequest ackRequest, String message){
        SocketIOSession.init();
        SocketIOSession.CLIENT_MAP.forEach((sessionId,client) -> {
            client.sendEvent("success",Message.newMessage("success"));
        });
        StringBuffer buffer = new StringBuffer();
        for(Map.Entry<String,Integer> entry : SocketIOSession.USER_MAP.entrySet()){
            String username = userMapper.getUser(entry.getValue()).getUsername();
            buffer.append(entry.getValue() + ":" + username + "-");
        }


    }
}
