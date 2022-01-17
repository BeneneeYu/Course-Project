package com.webproject.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
public class MessageHistory {

    int historyId;
    @NonNull private Timestamp time;
    @NonNull private int userId;
    @NonNull private String username;
    @NonNull private String messageType;
    @NonNull private String message;

}
