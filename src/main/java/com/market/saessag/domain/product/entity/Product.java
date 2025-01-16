package com.market.saessag.domain.product.entity;

import com.market.saessag.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String photo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long price;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String meetingPlace;

    private LocalDateTime addedDate;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public enum ProductStatus {
        FOR_SALE, HIDDEN, SOLD_OUT
    }

    @PrePersist
    public void prePersist() {
        this.addedDate = LocalDateTime.now(); // 현재 시간 자동 설정
    }
}
