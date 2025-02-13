package com.market.saessag.domain.chat.controller;

import com.market.saessag.domain.chat.dto.ChatMessageResponse;
import com.market.saessag.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/messages")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/{roomId}")
    public List<ChatMessageResponse> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return chatMessageService.getMessages(roomId, page, size);
    }
}
