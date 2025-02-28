package com.market.saessag.domain.chat.repository;

import com.market.saessag.domain.chat.entity.ChatMessage;
import com.market.saessag.domain.chat.entity.ChatMessageRead;
import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageReadRepository extends JpaRepository<ChatMessageRead, Long> {

    // 읽음 여부 판단 (이미 읽은 메시지일 시 true)
    boolean existsByChatMessageAndUser(ChatMessage message, User user);

    // 읽은 메시지 ID 반환
    @Query("SELECT r.chatMessage.id FROM ChatMessageRead r " +
            "WHERE r.user = :user AND r.chatMessage.id IN " +
            "(SELECT m.id FROM ChatMessage m WHERE m.chatRoom = :chatRoom)")
    List<Long> findMessageIdByUserAndChatRoom(User user, ChatRoom chatRoom);

}