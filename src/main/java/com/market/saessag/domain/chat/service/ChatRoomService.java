package com.market.saessag.domain.chat.service;

import com.market.saessag.domain.chat.dto.ChatRoomResponse;
import com.market.saessag.domain.chat.entity.ChatMessage;
import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.chat.entity.ChatSubscription;
import com.market.saessag.domain.chat.repository.ChatMessageRepository;
import com.market.saessag.domain.chat.repository.ChatRoomRepository;
import com.market.saessag.domain.chat.repository.ChatSubscriptionRepository;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.repository.ProductRepository;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSubscriptionRepository chatSubscriptionRepository;

    //방 생성 (방 존재 시 채팅방 반환)
    public ChatRoomResponse createOrGetChatRoom(Long productId, Long buyerId, Long sellerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("구매자를 찾을 수 없습니다."));

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("판매자를 찾을 수 없습니다."));

        ChatRoom chatRoom = chatRoomRepository.findByProductAndBuyerAndSeller(product, buyer, seller)
                .orElseGet(() -> {
                    ChatRoom newRoom = ChatRoom.builder()
                            .product(product)
                            .buyer(buyer)
                            .seller(seller)
                            .build();

                    return chatRoomRepository.save(newRoom);
                });

        subscribeUserToChatRoom(buyer, chatRoom);
        subscribeUserToChatRoom(seller, chatRoom);

        return chatRoomResponseEntity(chatRoom);
    }

    // 유저의 모든 채팅방 반환
    public List<ChatRoomResponse> getUserChatRooms(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<ChatRoom> chatRooms = chatRoomRepository.findByBuyerOrSeller(user, user);

        return chatRooms.stream()
                .map(this::chatRoomResponseEntity)
                .collect(Collectors.toList());
    }

    // ChatRoomResponse DTO 변환
    private ChatRoomResponse chatRoomResponseEntity(ChatRoom chatRoom) {
        ChatMessage lastMessage = chatMessageRepository
                .findTopByChatRoomOrderByTimeStampDesc(chatRoom)
                .orElse(null);

        return ChatRoomResponse.fromEntity(chatRoom,
                lastMessage != null ? lastMessage.getContent() : "메시지가 없습니다.",
                lastMessage != null ? lastMessage.getTimeStamp() : null);
    }

    // 유저가 채팅방을 구독하지 않고 있을 시 구독 설정
    private void subscribeUserToChatRoom(User user, ChatRoom chatRoom) {
        if (chatSubscriptionRepository.findByUserAndChatRoom(user, chatRoom).isEmpty()) {
            ChatSubscription subscription = ChatSubscription.builder()
                    .user(user)
                    .chatRoom(chatRoom)
                    .build();

            chatSubscriptionRepository.save(subscription);
        }
    }

    @Transactional
    public void leftChatRoom(Long roomId, HttpServletRequest request) {
        User user = getUserFromSession(request);

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        if (user == chatRoom.getBuyer()) {
            chatRoom.updateBuyerLeftAt();
        } else if (user == chatRoom.getSeller()) {
            chatRoom.updateSellerLeftAt();
        } else {
            throw new CustomException(ErrorCode.ROOM_HAS_NOT_USER);
        }

        chatRoomRepository.save(chatRoom);
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
