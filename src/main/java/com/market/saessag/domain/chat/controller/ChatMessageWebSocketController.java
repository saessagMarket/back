package com.market.saessag.domain.chat.controller;

import com.market.saessag.domain.chat.dto.ChatMessageRequest;
import com.market.saessag.domain.chat.dto.ChatMessageResponse;
import com.market.saessag.domain.chat.service.ChatMessageService;
import com.market.saessag.domain.chat.service.ChatSubscriptionService;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageWebSocketController {
    private final ChatMessageService chatMessageService;
    private final ChatSubscriptionService chatSubscriptionService;

    @MessageMapping("/chat/{roomId}/sendMessage")
    @SendTo("/topic/chat/{roomId}")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageRequest message) {
        ChatMessageResponse savedMessage = chatMessageService.saveMessage(roomId, message);

        //오프라인 구독자들에게 메시지 전송
        chatSubscriptionService.sendToOffSubscriber(savedMessage, roomId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, savedMessage));
    }
}