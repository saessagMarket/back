package com.market.saessag.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.market.saessag.domain.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null값인 필드는 응답에서 제외
public class ChatMessageResponse {
    private Long id;
    private Long roomId;
    private Long senderId;
    private String content;
    private LocalDateTime timeStamp;
    private Boolean isRead;

    public static ChatMessageResponse fromEntity(ChatMessage chatMessage, boolean isRead) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .roomId(chatMessage.getChatRoom().getId())
                .senderId(chatMessage.getSender().getId())
                .content(Optional.ofNullable(chatMessage.getContent()).orElse(""))
                .timeStamp(chatMessage.getTimeStamp())
                .isRead(isRead)
                .build();
    }

    public static ChatMessageResponse fromEntityForSearch(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .roomId(chatMessage.getChatRoom().getId())
                .senderId(chatMessage.getSender().getId())
                .content(Optional.ofNullable(chatMessage.getContent()).orElse(""))
                .timeStamp(chatMessage.getTimeStamp())
                .isRead(null)
                .build();
    }

}