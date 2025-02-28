package com.market.saessag.domain.chat.service;

import com.market.saessag.domain.chat.dto.ChatMessageRequest;
import com.market.saessag.domain.chat.dto.ChatMessageResponse;
import com.market.saessag.domain.chat.entity.ChatMessage;
import com.market.saessag.domain.chat.entity.ChatMessageRead;
import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.chat.repository.ChatMessageReadRepository;
import com.market.saessag.domain.chat.repository.ChatMessageRepository;
import com.market.saessag.domain.chat.repository.ChatRoomRepository;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageReadRepository chatMessageReadRepository;

    public ChatMessageResponse saveMessage(Long roomId, ChatMessageRequest message) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        User sender = userRepository.findById(message.getSenderId()) //세션으로 변경
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ChatMessage newMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(message.getContent())
                .timeStamp(LocalDateTime.now())
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(newMessage);

        return ChatMessageResponse.fromEntity(savedMessage, false);
    }

    public List<ChatMessageResponse> getMessages(Long roomId, HttpServletRequest httpRequest, int page, int size) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        Long userId = getUserFromSession(httpRequest).getId();

        Long receiverId = chatRoom.getBuyer().getId().equals(userId) ? chatRoom.getSeller().getId() : chatRoom.getBuyer().getId();

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ChatMessage> messages = chatMessageRepository.findByChatRoomOrderByTimeStampDesc(chatRoom, pageRequest);


        // 상대방이 안 읽은 메시지
        List<ChatMessage> unreadMessages = chatMessageRepository.findUnreadMessagesSentByUser(roomId, userId, receiverId);
        Set<Long> unreadMessageIds = unreadMessages.stream().map(ChatMessage::getId).collect(Collectors.toSet());

        return messages.stream()
                .map(message -> ChatMessageResponse.fromEntity(message, !unreadMessageIds.contains(message.getId())))
                .collect(Collectors.toList());
    }

    // 메시지 검색
    public List<ChatMessageResponse> searchMessages(Long roomId, String keyword){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomAndContentContainingOrderByTimeStampDesc(chatRoom, keyword);

        return messages.stream()
                .map(ChatMessageResponse::fromEntityForSearch)
                .collect(Collectors.toList());
    }

    // 읽음 처리
    @Transactional
    public void markMessagesAsRead(Long roomId, HttpServletRequest httpRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        User user = getUserFromSession(httpRequest);

        List<ChatMessage> unreadMessages = chatMessageRepository.findByChatRoom(chatRoom);

        // ChatMessageRead 테이블에 안 읽은 메시지 전부 저장
        for (ChatMessage message : unreadMessages) {
            if (!chatMessageReadRepository.existsByChatMessageAndUser(message, user)) {
                ChatMessageRead chatMessageRead = ChatMessageRead.builder()
                        .chatMessage(message)
                        .user(user)
                        .isRead(true)
                        .build();
                chatMessageReadRepository.save(chatMessageRead);
            }
        }
    }

    // 안 읽은 메시지 수 카운트
    @Transactional
    public Long getUnreadMessageCount(Long roomId, HttpServletRequest httpRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        User user = getUserFromSession(httpRequest);

        List<Long> readMessageIds = chatMessageReadRepository.findMessageIdByUserAndChatRoom(user, chatRoom);
        if (readMessageIds.isEmpty()) {
            return chatMessageRepository.countByChatRoom(chatRoom);
        }

        return chatMessageRepository.countByChatRoomAndIdNotIn(chatRoom, readMessageIds);
    }

    // 안 읽은 메시지 조회
    public List<ChatMessageResponse> getUnreadMessages(Long roomId, HttpServletRequest httpRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        User user = getUserFromSession(httpRequest);

        // 읽은 메시지 테이블에서 특정 채팅방에서 해당 유저가 읽은 메시지의 ID 리스트를 반환
        List<Long> readMessageIds = chatMessageReadRepository.findMessageIdByUserAndChatRoom(user, chatRoom);

        List<ChatMessage> unreadMessages;
        if (readMessageIds.isEmpty()) {
            unreadMessages = chatMessageRepository.findByChatRoom(chatRoom);
        } else {
            unreadMessages = chatMessageRepository.findByChatRoomAndIdNotIn(chatRoom, readMessageIds);
        }

        return unreadMessages.stream()
                .map(message -> ChatMessageResponse.fromEntity(message, false))
                .collect(Collectors.toList());
    }

    // 세션에서 사용자 정보를 가져와서 검증
    private User getUserFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SignInResponse userSession = (SignInResponse) session.getAttribute("userProfile");

        if (userSession == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return userRepository.findById(userSession.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
