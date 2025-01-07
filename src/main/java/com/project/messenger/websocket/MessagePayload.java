package com.project.messenger.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessagePayload {
    private String message;

    public String getMessage() { return message; }

}
