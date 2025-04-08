package com.market.saessag.domain.chat.controller;

import com.market.saessag.domain.chat.dto.ChatFileRequest;
import com.market.saessag.domain.chat.dto.ChatFileResponse;
import com.market.saessag.domain.chat.dto.ChatMessageRequest;
import com.market.saessag.domain.chat.dto.ChatMessageResponse;
import com.market.saessag.domain.chat.service.ChatFileService;
import com.market.saessag.domain.chat.service.ChatMessageService;
import com.market.saessag.domain.chat.service.ChatSubscriptionService;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
    public ApiResponse<ChatMessageResponse> sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageRequest message,
                                                        SimpMessageHeaderAccessor headerAccessor) {

        ChatMessageResponse savedMessage = chatMessageService.saveMessage(roomId, message, headerAccessor);

        //메시지 읽음 처리
        SignInResponse user = (SignInResponse) headerAccessor.getSessionAttributes().get("userProfile");
        chatMessageService.markMessagesAsRead(roomId, user);

        //오프라인 구독자들에게 메시지 전송
        chatSubscriptionService.sendToOffSubscriber(savedMessage, roomId);
        return ApiResponse.success(savedMessage);
    }

    // 파일 전송
    @MessageMapping("/chat/{roomId}/sendFile")
    @SendTo("/topic/chat/{roomId}")
    public ApiResponse<List<ChatFileResponse>> sendFile(@DestinationVariable Long roomId, @Payload ChatFileRequest fileRequest) {
        List<ChatFileResponse> savedFile = chatFileService.saveFile(roomId, fileRequest);

        return ApiResponse.success(savedFile);
    }
}