package com.webproject.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class GameHistory {
    private int gameId;
    private int step;
    private String users;
    private Timestamp time;
}
