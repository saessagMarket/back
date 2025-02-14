package com.market.saessag.domain.chat.controller;

import com.market.saessag.domain.chat.dto.ChatRoomRequest;
import com.market.saessag.domain.chat.dto.ChatRoomResponse;
import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.chat.service.ChatRoomService;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping()
    public ResponseEntity<ApiResponse<ChatRoomResponse>> createChatRoom(@RequestBody ChatRoomRequest request) {
        System.out.println("request.getSellerId() = " + request.getSellerId());
        ChatRoomResponse chatRoom = chatRoomService.createOrGetChatRoom(request.getProductId(), request.getBuyerId(), request.getSellerId());

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, chatRoom));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<ChatRoomResponse>>> getChatRoom(@PathVariable Long userId) {
        List<ChatRoomResponse> chatRooms = chatRoomService.getUserChatRooms(userId);

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, chatRooms));
    }

}