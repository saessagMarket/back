package com.market.saessag.domain.chat.dto;

import com.market.saessag.global.response.SuccessCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomCreateResponse {
    private SuccessCode successCode;
    private ChatRoomResponse chatRoomResponse;
}
