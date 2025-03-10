package com.market.saessag.domain.chat.repository;

import com.market.saessag.domain.chat.entity.ChatMessage;
import com.market.saessag.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 해당 채팅방의 최신 메시지부터 반환 (퇴장후만 보이도록 수정)
    @Query("SELECT m FROM ChatMessage m " +
            "WHERE m.chatRoom.id=:roomId " +
            "AND ((m.sender.id = :userId AND (m.timeStamp > :leftAt OR :leftAt IS NULL))" +
            "OR (m.sender.id <> :userId AND (m.timeStamp>:leftAt OR :leftAt IS NULL)))" +
            "ORDER BY m.timeStamp DESC")
    Page<ChatMessage> findMessagesAfterLeftTime(
            @Param("roomId") Long roomId,
            @Param("userId") Long userId,
            @Param("leftAt") LocalDateTime leftAt,
            PageRequest pageRequest);

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

    Long countByChatRoom(ChatRoom chatRoom);

    // 상대방이 안 읽은 메시지 목록
    @Query("SELECT m FROM ChatMessage m " +
            "WHERE m.chatRoom.id = :chatRoomId " +
            "AND m.sender.id = :senderId " +
            "AND m.id NOT IN (SELECT r.chatMessage.id FROM ChatMessageRead r WHERE r.user.id = :receiverId)")
    List<ChatMessage> findUnreadMessagesSentByUser(
            @Param("chatRoomId") Long chatRoomId,
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId
    );

    // 특정 채팅방 퇴장 이후 메시지 존재 여부 반환
    boolean existsByChatRoomAndTimeStampAfter(ChatRoom room, LocalDateTime leftAt);

    List<ChatMessage> findByChatRoomAndContentContainingAndTimeStampAfterOrderByTimeStampDesc(ChatRoom chatRoom, String keyword, LocalDateTime leftAt);
}