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
import java.util.HashSet;
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

    // 특정 채팅방 메시지 조회
    public List<ChatMessageResponse> getMessages(Long roomId, HttpServletRequest httpRequest, int page, int size) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        User user = getUserFromSession(httpRequest);

        Page<ChatMessage> messages;
        PageRequest pageRequest = PageRequest.of(page, size);

        if (chatRoom.getBuyer().equals(user) || chatRoom.getSeller().equals(user)) { // 해당 채팅방에 속해 있는 유저인지 확인
            LocalDateTime leftAt = chatRoom.getBuyer().equals(user) ? chatRoom.getBuyerLeftAt() : chatRoom.getSellerLeftAt();

            if (leftAt == null) { // 퇴장한 적 없는 경우
                messages = chatMessageRepository.findByChatRoomOrderByTimeStampDesc(chatRoom, pageRequest);
            } else {
                messages = chatMessageRepository.findByChatRoomAndTimeStampAfterOrderByTimeStampDesc(chatRoom, leftAt, pageRequest);
            }

        } else {
            throw new CustomException(ErrorCode.ROOM_HAS_NOT_USER);
        }

        // 상대방이 읽은 메시지
        User receiver = chatRoom.getBuyer().equals(user) ? chatRoom.getSeller() : chatRoom.getBuyer();
        Set<Long> readMessageIds = new HashSet<>(getReadMessageIdsFromChatRoom(chatRoom, receiver));

        // 채팅방 내에 표시할 읽음 여부 추가하여 반환
        return messages.stream()
                .map(message -> ChatMessageResponse.fromEntity(message, readMessageIds.contains(message.getId())))
                .collect(Collectors.toList());
    }

    // 메시지 검색
    public List<ChatMessageResponse> searchMessages(Long roomId, String keyword, HttpServletRequest httpRequest) {
        User user = getUserFromSession(httpRequest);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        // 유저가 해당 채팅방 떠난 시간
        LocalDateTime leftAt = chatRoom.getBuyer().equals(user) ? chatRoom.getBuyerLeftAt() : chatRoom.getSellerLeftAt();

        List<ChatMessage> messages;
        if (leftAt == null) {
            messages = chatMessageRepository.findByChatRoomAndContentContainingOrderByTimeStampDesc(chatRoom, keyword);
        } else {
            messages = chatMessageRepository.findByChatRoomAndContentContainingAndTimeStampAfterOrderByTimeStampDesc(chatRoom, keyword, leftAt);
        }

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

        List<Long> readMessageIds = getReadMessageIdsFromChatRoom(chatRoom, user);

        // 유저가 해당 채팅방 떠난 시간
        LocalDateTime leftAt = chatRoom.getBuyer().equals(user) ? chatRoom.getBuyerLeftAt() : chatRoom.getSellerLeftAt();

        if (leftAt == null) {
            if (readMessageIds.isEmpty()) {
                return chatMessageRepository.countByChatRoom(chatRoom); // 삭제 ?
            } else {
                return chatMessageRepository.countByChatRoomAndIdNotIn(chatRoom, readMessageIds);
            }
        } else {
            if (readMessageIds.isEmpty()) {
                return chatMessageRepository.countByChatRoomAndTimeStampAfter(chatRoom, leftAt);
            } else {
                return chatMessageRepository.countByChatRoomAndIdNotInAndTimeStampAfter(chatRoom, readMessageIds, leftAt);
            }
        }
    }

    // 안 읽은 메시지 조회
    public List<ChatMessageResponse> getUnreadMessages(Long roomId, HttpServletRequest httpRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        User user = getUserFromSession(httpRequest);

        // 읽은 메시지
        List<Long> readMessageIds = getReadMessageIdsFromChatRoom(chatRoom, user);

        // 유저가 해당 채팅방 떠난 시간
        LocalDateTime leftAt = chatRoom.getBuyer().equals(user) ? chatRoom.getBuyerLeftAt() : chatRoom.getSellerLeftAt();

        List<ChatMessage> unreadMessages;

        if (leftAt == null) {
            if (readMessageIds.isEmpty()) {
                unreadMessages = chatMessageRepository.findByChatRoom(chatRoom);
            } else {
                unreadMessages = chatMessageRepository.findByChatRoomAndIdNotIn(chatRoom, readMessageIds);
            }
        } else {
            if (readMessageIds.isEmpty()) {
                unreadMessages = chatMessageRepository.findByChatRoomAndTimeStampAfter(chatRoom, leftAt);
            } else {
                unreadMessages = chatMessageRepository.findByChatRoomAndIdNotInAndTimeStampAfter(chatRoom, readMessageIds, leftAt);
            }
        }

        return unreadMessages.stream()
                .map(message -> ChatMessageResponse.fromEntity(message, false))
                .collect(Collectors.toList());
    }


    // 사용자가 특정 채팅방에서 읽은 메시지 ID 리스트 반환
    private List<Long> getReadMessageIdsFromChatRoom(ChatRoom chatRoom, User user) {
        // 특정 채팅방 전체 메시지 리스트
        List<ChatMessage> chatRoomMessages = chatMessageRepository.findByChatRoom(chatRoom);

        return chatMessageReadRepository.findByUserAndChatMessageIn(user, chatRoomMessages).stream()
                .map(r -> r.getChatMessage().getId())
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
