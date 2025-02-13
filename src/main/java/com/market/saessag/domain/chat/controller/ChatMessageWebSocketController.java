package com.market.saessag.domain.chat.controller;

import com.market.saessag.domain.chat.dto.ChatMessageRequest;
import com.market.saessag.domain.chat.dto.ChatMessageResponse;
import com.market.saessag.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageWebSocketController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/{roomId}/sendMessage")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessageResponse sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageRequest message) {
        return chatMessageService.saveMessage(roomId, message);
    }
}