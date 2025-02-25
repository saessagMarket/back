package com.market.saessag.domain.chat.service;

import com.market.saessag.domain.chat.dto.ChatMessageRequest;
import com.market.saessag.domain.chat.dto.ChatMessageResponse;
import com.market.saessag.domain.chat.entity.ChatMessage;
import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.chat.repository.ChatMessageRepository;
import com.market.saessag.domain.chat.repository.ChatRoomRepository;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatMessageResponse saveMessage(Long roomId, ChatMessageRequest message) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방이 없습니다."));

        User sender = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        ChatMessage newMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(message.getContent())
                .timeStamp(LocalDateTime.now())
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(newMessage);

        return ChatMessageResponse.fromEntity(savedMessage);
    }

    public List<ChatMessageResponse> getMessages(Long roomId, int page, int size) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다."));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ChatMessage> messages = chatMessageRepository.findByChatRoomOrderByTimeStampDesc(chatRoom, pageRequest);

        return messages.stream()
                .map(ChatMessageResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 메시지 검색
    public List<ChatMessageResponse> searchMessages(Long roomId, String keyword){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방이 없습니다."));

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomAndContentContainingOrderByTimeStampDesc(chatRoom, keyword);

        return messages.stream()
                .map(ChatMessageResponse::fromEntity)
                .collect(Collectors.toList());
    }

}
