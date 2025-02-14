package com.market.saessag.domain.chat.dto;

import com.market.saessag.domain.chat.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponse {
    private Long roomId;
    private Long productId;
    private Long buyerId;
    private Long sellerId;
    private String productTitle;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public static ChatRoomResponse fromEntity(ChatRoom chatRoom, String lastMessage, LocalDateTime lastMessageTime) {
        return ChatRoomResponse.builder()
                .roomId(chatRoom.getId())
                .productId(chatRoom.getProductId().getId())
                .productTitle(chatRoom.getProductId().getTitle())
                .buyerId(chatRoom.getBuyerId().getId())
                .sellerId(chatRoom.getSellerId().getId())
                .lastMessage(lastMessage)
                .lastMessageTime(lastMessageTime)
                .build();
    }
}