package com.market.saessag.domain.chat.repository;

import com.market.saessag.domain.chat.entity.ChatMessage;
import com.market.saessag.domain.chat.entity.ChatMessageRead;
import com.market.saessag.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageReadRepository extends JpaRepository<ChatMessageRead, Long> {

    // 읽음 여부 판단 (이미 읽은 메시지일 시 true)
    boolean existsByChatMessageAndUser(ChatMessage message, User user);

    // 읽은 메시지 객체 리스트 반환
    List<ChatMessageRead> findByUserAndChatMessageIn(User user, List<ChatMessage> readMessageIds);
}