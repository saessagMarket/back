package com.market.saessag.domain.product.entity;

import com.market.saessag.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection
    private List<String> photo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long price;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double latitude;

    private Double longitude;

    private String basicAddress;

    private String detailedAddress;

    private LocalDateTime addedDate;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private LocalDateTime bumpAt;


    public enum ProductStatus {
        FOR_SALE, HIDDEN, SOLD_OUT
    }

    @PrePersist
    public void prePersist() {
        this.addedDate = LocalDateTime.now(); // 현재 시간 자동 설정
    }

    public void updateProduct(String title, Long price, String description, List<String> photo,
                              Double latitude, Double longitude, String basicAddress, String detailedAddress, ProductStatus status) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.basicAddress = basicAddress;
        this.detailedAddress = detailedAddress;
        this.status = status;
    }

    public void updateBumpAt(LocalDateTime bumpAt) {
        this.bumpAt = bumpAt;
    }

    public void updateStatus(ProductStatus status) {
        this.status = status;
    }
}
