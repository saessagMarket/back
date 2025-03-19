package com.market.saessag.domain.chat.controller;

import com.market.saessag.domain.chat.dto.ChatRoomRequest;
import com.market.saessag.domain.chat.dto.ChatRoomResponse;
import com.market.saessag.domain.chat.service.ChatRoomService;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 방 생성
    @PostMapping()
    public ApiResponse<ChatRoomResponse> createChatRoom(@RequestBody ChatRoomRequest request) {
        ChatRoomResponse chatRoom = chatRoomService.createOrGetChatRoom(request.getProductId(), request.getBuyerId(), request.getSellerId());

        return ApiResponse.success(SuccessCode.ROOM_CREATED, chatRoom);
    }

    // 특정 유저가 현재 속해있는 채팅방 리스트 반환
    @GetMapping()
    public ApiResponse<List<ChatRoomResponse>> getChatRoom(HttpServletRequest request) {
        List<ChatRoomResponse> chatRooms = chatRoomService.getUserChatRooms(request);

        return ApiResponse.success(SuccessCode.DATA_FETCHED, chatRooms);
    }

    // 방 퇴장
    @PostMapping("/left/{roomId}")
    public ApiResponse<Void> leftChatRoom(@PathVariable Long roomId, HttpServletRequest request) {
        chatRoomService.leftChatRoom(roomId, request);
        return ApiResponse.success(SuccessCode.OK);
    }

}