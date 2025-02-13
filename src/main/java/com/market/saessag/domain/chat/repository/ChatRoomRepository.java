package com.market.saessag.domain.chat.repository;

import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByProductIdAndBuyerIdAndSellerId(Product product, User buyer, User seller);

    List<ChatRoom> findByBuyerIdOrSellerId(User buyer, User seller);
}