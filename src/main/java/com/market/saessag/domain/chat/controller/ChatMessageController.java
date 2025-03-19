package com.market.saessag.domain.chat.controller;

import com.market.saessag.domain.chat.dto.ChatMessageResponse;
import com.market.saessag.domain.chat.service.ChatMessageService;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<ChatMessageResponse> messages = chatMessageService.getMessages(roomId, request, page, size);
        return ApiResponse.success(SuccessCode.DATA_FETCHED, messages);
    }

    // 채팅방 내 메시지 검색
    @GetMapping("/{roomId}/search")
    public ApiResponse<List<ChatMessageResponse>> searchMessages(
            @PathVariable Long roomId,
            @RequestParam String keyword,
            HttpServletRequest request) {
        return ApiResponse.success(SuccessCode.DATA_FETCHED, chatMessageService.searchMessages(roomId, keyword, request));
    }

    // 읽음 처리
    @PostMapping("/{roomId}/mark-read")
    public ApiResponse<Void> markMessageAsRead(@PathVariable Long roomId, HttpServletRequest request) {
        // 변경된 세션 처리 통합한 후 수정하겠습니다!
        HttpSession session = request.getSession();
        SignInResponse userSession = (SignInResponse) session.getAttribute("userProfile");

        chatMessageService.markMessagesAsRead(roomId, userSession);
        return ApiResponse.success();
    }

    // 안 읽은 메시지 목록 조회
    @GetMapping("/{roomId}/unread-list")
    public ApiResponse<List<ChatMessageResponse>> getUnreadMessages(@PathVariable Long roomId, HttpServletRequest request) {
        List<ChatMessageResponse> unreadMessages = chatMessageService.getUnreadMessages(roomId, request);
        return ApiResponse.success(SuccessCode.DATA_FETCHED, unreadMessages);
    }

    // 안 읽은 메시지 개수 조회
    @GetMapping("/{roomId}/unread-count")
    public ApiResponse<Long> unreadMessageCount(@PathVariable Long roomId, HttpServletRequest request) {
        Long unreadCount = chatMessageService.getUnreadMessageCount(roomId, request);
        return ApiResponse.success(SuccessCode.DATA_FETCHED, unreadCount);
    }


}
