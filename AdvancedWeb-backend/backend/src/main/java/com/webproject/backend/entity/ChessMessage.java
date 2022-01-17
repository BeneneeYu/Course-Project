package com.webproject.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ChessMessage {

    @NonNull private int id;
    @NonNull private String position;
    @NonNull private int location;
}
