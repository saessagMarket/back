package com.market.saessag.domain.chat.repository;

import com.market.saessag.domain.chat.entity.ChatRoom;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 특정 상품, 구매자, 판매자에 매칭되는 채팅방 반환
    Optional<ChatRoom> findByProductAndBuyerAndSeller(Product product, User buyer, User seller);

    // 특정 유저의 모든 채팅방 반환 (퇴장 채팅방 제외)
    @Query("SELECT r FROM ChatRoom r " +
            "WHERE (r.buyer=:user AND (r.buyerLeftAt IS NULL " +
            "OR EXISTS (SELECT 1 FROM ChatMessage m WHERE m.chatRoom=r AND m.timeStamp>r.buyerLeftAt)))" +
            "OR (r.seller=:user AND (r.sellerLeftAt IS NULL " +
            "OR EXISTS (SELECT 1 FROM ChatMessage m WHERE m.chatRoom=r AND m.timeStamp>r.sellerLeftAt)))")
    List<ChatRoom> findByBuyerOrSeller(@Param("user") User user);


}