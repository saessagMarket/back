package com.market.saessag.product.entity;

import com.market.saessag.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

    @Column(nullable = false)
    private LocalDate addedDate;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;


    public enum ProductStatus {
        판매중, 숨기기, 판매완료
    }
}
