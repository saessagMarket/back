package com.market.saessag.domain.product.entity;

import com.market.saessag.domain.product.dto.ProductRequest;
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

    private LocalDateTime updatedAt; // 수정 시점

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public enum ProductStatus {
        FOR_SALE, HIDDEN, SOLD_OUT
    }

    @PrePersist
    public void prePersist() {
        this.addedDate = LocalDateTime.now(); // 현재 시간 자동 설정
    }

    public void updateStatus(ProductStatus status) {
        this.status = status;
    }

    // 끌어올리기 전용 메서드
    public void bump() { this.updatedAt = LocalDateTime.now(); }

    // 상품 생성(정적 팩토리 메서드)
    public static Product createProduct(User user, ProductRequest request) {
        Product product = new Product();
        product.user = user;
        product.title = request.getTitle();
        product.price = request.getPrice();
        product.description = request.getDescription();
        product.photo = request.getPhoto();
        product.latitude = request.getLatitude();
        product.longitude = request.getLongitude();
        product.basicAddress = request.getBasicAddress();
        product.detailedAddress = request.getDetailedAddress();
        product.status = ProductStatus.valueOf(request.getStatus());
        product.updatedAt = LocalDateTime.now();
        return product;
    }

    // 상품 정보 수정(정적 팩토리 메서드)
    public void updateProduct(ProductRequest request) {
        this.title = request.getTitle();
        this.price = request.getPrice();
        this.description = request.getDescription();
        this.photo = request.getPhoto();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
        this.basicAddress = request.getBasicAddress();
        this.detailedAddress = request.getDetailedAddress();
        this.status = ProductStatus.valueOf(request.getStatus());
        this.updatedAt = LocalDateTime.now();
    }
}
