package com.market.saessag.domain.chat.controller;

import com.market.saessag.domain.chat.dto.ChatRoomRequest;
import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomService.createOrGetChatRoom(request.getProductId(), request.getBuyerId(), request.getSellerId());

        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/room/{userId}")
    public ResponseEntity<List<ChatRoom>> getChatRoom(@PathVariable Long userId) {
        return ResponseEntity.ok(chatRoomService.getUserChatRooms(userId));
    }

}