package com.market.saessag.domain.chat.service;

import com.market.saessag.domain.chat.dto.ChatMessageResponse;
import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.chat.entity.ChatSubscription;
import com.market.saessag.domain.chat.repository.ChatRoomRepository;
import com.market.saessag.domain.chat.repository.ChatSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatSubscriptionService {
    private final ChatSubscriptionRepository chatSubscriptionRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpUserRegistry simpUserRegistry;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendToOffSubscriber(ChatMessageResponse savedMessage, Long roomId) {
        // DB에서 해당 채팅방을 구독한 사용자 목록 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방이 없습니다."));
        List<ChatSubscription> subscriptions = chatSubscriptionRepository.findByChatRoom(chatRoom);

        for (ChatSubscription subscription : subscriptions) {
            Long userId = subscription.getUser().getId();
            String userSessionId = "user-" + userId;

            // 현재 WebSocket을 구독하고 있는지 확인
            boolean isOnline = simpUserRegistry.getUser(userSessionId) != null;

            // 오프라인 사용자에게만 메시지 전송
            if (!isOnline) {
                messagingTemplate.convertAndSend("/queue/user-" + userId, savedMessage);
            }
        }
    }
}