package com.market.saessag.domain.chat.repository;

import com.market.saessag.domain.chat.entity.ChatMessage;
import com.market.saessag.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 해당 채팅방의 최신 메시지부터 반환
    Page<ChatMessage> findByChatRoomOrderByTimeStampDesc(ChatRoom chatRoom,PageRequest pageRequest);

    // 해당 채팅방의 최신 메시지 1개 반환
    Optional<ChatMessage> findTopByChatRoomOrderByTimeStampDesc(ChatRoom chatRoom);

    // 특정 채팅방에서 특정 키워드 검색
    List<ChatMessage> findByChatRoomAndContentContainingOrderByTimeStampDesc(ChatRoom chatRoom, String keyword);
}