package com.webproject.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageInfo {

    int userId;
    String username;
    String message;
    String messageType;
}
