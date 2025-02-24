package com.market.saessag.domain.chat.controller;

import com.market.saessag.domain.chat.dto.ChatMessageResponse;
import com.market.saessag.domain.chat.service.ChatMessageService;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/messages")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    // 채팅방 전체 메시지 반환
    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<ChatMessageResponse> messages = chatMessageService.getMessages(roomId, page, size);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, messages));
    }
}
