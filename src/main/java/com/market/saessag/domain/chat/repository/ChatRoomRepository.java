package com.market.saessag.domain.chat.repository;

import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 특정 상품, 구매자, 판매자에 매칭되는 채팅방 반환
    Optional<ChatRoom> findByProductAndBuyerAndSeller(Product product, User buyer, User seller);

    // 특정 유저가 속한 모든 채팅방 반환
    List<ChatRoom> findByBuyerOrSeller(User buyer, User seller);
}