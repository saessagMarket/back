package com.market.saessag.domain.chat.controller;

import com.market.saessag.domain.chat.dto.ChatFileRequest;
import com.market.saessag.domain.chat.dto.ChatFileResponse;
import com.market.saessag.domain.chat.dto.ChatMessageRequest;
import com.market.saessag.domain.chat.dto.ChatMessageResponse;
import com.market.saessag.domain.chat.service.ChatFileService;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatMessageWebSocketController {
    private final ChatMessageService chatMessageService;
    private final ChatSubscriptionService chatSubscriptionService;
    private final ChatFileService chatFileService;

    // 메시지 전송
    @MessageMapping("/chat/{roomId}/sendMessage")
    @SendTo("/topic/chat/{roomId}")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageRequest message) {
        ChatMessageResponse savedMessage = chatMessageService.saveMessage(roomId, message);

        //오프라인 구독자들에게 메시지 전송
        chatSubscriptionService.sendToOffSubscriber(savedMessage, roomId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, savedMessage));
    }

    // 파일 전송
    @MessageMapping("/chat/{roomId}/sendFile")
    @SendTo("/topic/chat/{roomId}")
    public ResponseEntity<ApiResponse<List<ChatFileResponse>>> sendFile(@DestinationVariable Long roomId, @Payload ChatFileRequest fileRequest) {
        List<ChatFileResponse> savedFile = chatFileService.saveFile(roomId, fileRequest);

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, savedFile));
    }
}