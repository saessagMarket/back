package com.market.saessag.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChatMessageRequest {
    private Long senderId;
    private String content;
    private LocalDateTime timeStamp;
}
