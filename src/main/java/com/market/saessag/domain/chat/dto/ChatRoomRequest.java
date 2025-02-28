package com.market.saessag.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChatRoomRequest {
    private Long productId;
    private Long buyerId;
    private Long sellerId;
}