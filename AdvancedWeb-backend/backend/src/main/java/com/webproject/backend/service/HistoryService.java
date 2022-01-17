package com.webproject.backend.service;

import com.webproject.backend.entity.GameHistory;
import com.webproject.backend.entity.MessageHistory;
import com.webproject.backend.entity.MessageInfo;
import com.webproject.backend.mapper.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class HistoryService {

    private HistoryMapper historyMapper;

    @Autowired
    public HistoryService(HistoryMapper historyMapper) {
        this.historyMapper = historyMapper;
    }

    public void pushChatHistory(MessageInfo messageInfo) {
        Date date = new Date(System.currentTimeMillis());
        MessageHistory messageHistory = new MessageHistory();
        messageHistory.setTime(new Timestamp(date.getTime()));
        messageHistory.setUserId(messageInfo.getUserId());
        messageHistory.setUsername(messageInfo.getUsername());
        messageHistory.setMessageType(messageInfo.getMessageType());
        messageHistory.setMessage(messageInfo.getMessage());
        historyMapper.addHistory(messageHistory);
    }

    public void pushGameHistory(int step, String users){

    }
}
