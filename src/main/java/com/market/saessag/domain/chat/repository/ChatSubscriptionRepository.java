package com.market.saessag.domain.chat.repository;

import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.chat.entity.ChatSubscription;
import com.market.saessag.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatSubscriptionRepository extends JpaRepository<ChatSubscription, Long> {
    Optional<ChatSubscription> findByUserAndChatRoom(User user, ChatRoom chatRoom);

    List<ChatSubscription> findByChatRoom(ChatRoom chatRoom);  // 특정 채팅방을 구독한 모든 사용자 조회

    void deleteByUserAndChatRoom(User user, ChatRoom chatRoom);  // 특정 유저의 구독 해제
}
