package com.market.saessag.domain.chat.entity;

import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    private LocalDateTime buyerLeftAt;  // 구매자 나간 시간
    private LocalDateTime sellerLeftAt; // 판매자 나간 시간

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> message = new ArrayList<>();


    // 구매자 퇴장 시간 업데이트
    public void updateBuyerLeftAt() {
        this.buyerLeftAt = LocalDateTime.now();
    }

    // 판매자 퇴장 시간 업데이트
    public void updateSellerLeftAt() {
        this.sellerLeftAt = LocalDateTime.now();
    }

}
