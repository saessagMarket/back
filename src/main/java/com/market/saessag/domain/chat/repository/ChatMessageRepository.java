package com.market.saessag.domain.chat.repository;

import com.market.saessag.domain.chat.entity.ChatMessage;
import com.market.saessag.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 해당 채팅방의 최신 메시지 1개 반환
    Optional<ChatMessage> findTopByChatRoomOrderByTimeStampDesc(ChatRoom chatRoom);

    // 특정 채팅방에서 특정 키워드 검색
    List<ChatMessage> findByChatRoomAndContentContainingOrderByTimeStampDesc(ChatRoom chatRoom, String keyword);

    // 해당 채팅방의 메시지 리스트 반환
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);

    // 특정 사용자가 읽지 않은 메시지 반환
    List<ChatMessage> findByChatRoomAndIdNotIn(ChatRoom chatRoom, List<Long> readMessageIds);

    // 특정 사용자가 읽지 않은 메시지 개수 반환
    Long countByChatRoomAndIdNotIn(ChatRoom chatRoom, List<Long> readMessageIds);

    // 채팅방 전체 메시지 수 반환
    Long countByChatRoom(ChatRoom chatRoom);

    // 마지막 퇴장 이후 특정 사용자가 읽지 않은 메시지 개수 반환
    Long countByChatRoomAndIdNotInAndTimeStampAfter(ChatRoom chatRoom, List<Long> readMessageIds, LocalDateTime leftAt);

    // 마지막 퇴장 이후 채팅방 전체 메시지 수 반환
    Long countByChatRoomAndTimeStampAfter(ChatRoom chatRoom, LocalDateTime leftAt);

    // 특정 채팅방 퇴장 이후 메시지 존재 여부 반환
    boolean existsByChatRoomAndTimeStampAfter(ChatRoom room, LocalDateTime leftAt);

    // 마지막 퇴장 이후 해당 채팅방에서 특정 키워드를 포함하는 메시지 반환 (최신 순)
    List<ChatMessage> findByChatRoomAndContentContainingAndTimeStampAfterOrderByTimeStampDesc(ChatRoom chatRoom, String keyword, LocalDateTime leftAt);

    // 마지막 퇴장 이후 특정 채팅방 읽지 않은 메시지 반환
    List<ChatMessage> findByChatRoomAndIdNotInAndTimeStampAfter(ChatRoom chatRoom, List<Long> readMessageIds, LocalDateTime leftAt);

    // 마지막 퇴장 이후 특정 채팅방 메시지 전체 반환
    List<ChatMessage> findByChatRoomAndTimeStampAfter(ChatRoom chatRoom, LocalDateTime leftAt);

    // 특정 채팅방 메시지 전체 반환 (최신 순)
    Page<ChatMessage> findByChatRoomOrderByTimeStampDesc(ChatRoom chatRoom, PageRequest pageRequest);

    // 마지막 퇴장 이후 특정 채팅방 메시지 전체 반환 (최신 순, 페이징)
    Page<ChatMessage> findByChatRoomAndTimeStampAfterOrderByTimeStampDesc(ChatRoom chatRoom, LocalDateTime timeStampAfter, Pageable pageable);
}