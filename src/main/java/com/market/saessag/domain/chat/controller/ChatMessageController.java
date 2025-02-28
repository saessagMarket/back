package com.market.saessag.domain.chat.controller;

import com.market.saessag.domain.chat.dto.ChatMessageResponse;
import com.market.saessag.domain.chat.service.ChatMessageService;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
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
    public ApiResponse<List<ChatMessageResponse>> getMessages(
            @PathVariable Long roomId,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<ChatMessageResponse> messages = chatMessageService.getMessages(roomId, userId, page, size);
        return ApiResponse.success(SuccessCode.OK, messages);
    }

    // 채팅방 내 메시지 검색
    @GetMapping("/{roomId}/search")
    public ApiResponse<List<ChatMessageResponse>> searchMessages(
            @PathVariable Long roomId,
            @RequestParam String keyword) {
        return ApiResponse.success(SuccessCode.OK, chatMessageService.searchMessages(roomId, keyword));
    }

    // 읽음 처리
    @PostMapping("/{roomId}/mark-read")
    public ApiResponse<Void> markMessageAsRead(@PathVariable Long roomId, HttpServletRequest request) { //user 세션으로 바꿀 것
        chatMessageService.markMessagesAsRead(roomId, request);
        return ApiResponse.success(SuccessCode.OK, null);
    }

    // 안 읽은 메시지 목록 조회
    @GetMapping("/{roomId}/unread-list")
    public ApiResponse<List<ChatMessageResponse>> getUnreadMessages(@PathVariable Long roomId, HttpServletRequest request) {
        List<ChatMessageResponse> unreadMessages = chatMessageService.getUnreadMessages(roomId, request);
        return ApiResponse.success(SuccessCode.OK, unreadMessages);
    }

    // 안 읽은 메시지 개수 조회
    @GetMapping("/{roomId}/unread-count")
    public ApiResponse<Long> unreadMessageCount(@PathVariable Long roomId, HttpServletRequest request) {
        Long unreadCount = chatMessageService.getUnreadMessageCount(roomId, request);
        return ApiResponse.success(SuccessCode.OK, unreadCount);
    }


}
