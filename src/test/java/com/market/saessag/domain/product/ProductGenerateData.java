package com.market.saessag.domain.product;

import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.repository.ProductRepository;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
@ActiveProfiles("test")
public class ProductGenerateData {
    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private UserRepository userRepository;
    @Test
    public void 상품_데이터_생성() {
        Random random = new Random();

        // 임의의 User 생성 및 저장
        User user = User.builder()
                .email("test@gmail.com")
                .password("0000")
                .profileUrl("photo1.jpg")
                .nickname("테스트 닉네임")
                .role("ROLE_USER")
                .build();
        userRepository.save(user);

        for (int i = 1; i <= 100; i++) {
            Product product = new Product();
            // 기본 필드 설정
            product.setTitle("상품명 " + i);
            product.setPrice((long) (1000 + random.nextInt(9000))); // 1000 ~ 10000 사이의 값
            product.setDescription("상품의 설명 " + i);

            // 위치 관련 필드 설정
            product.setLatitude(37.5 + random.nextDouble()); // 위도: 37.5 ~ 38.5 사이의 랜덤 값
            product.setLongitude(127.0 + random.nextDouble()); // 경도: 127.0 ~ 128.0 사이의 랜덤 값
            product.setBasicAddress("도로명 주소 " + (random.nextInt(10) + 1));
            product.setDetailedAddress("상세 주소 " + (random.nextInt(100) + 1));

            // 사진 리스트 설정
            product.setPhoto(List.of("photo1.jpg", "photo2.jpg", "photo3.jpg"));

            // 상태 설정
            product.setStatus(Product.ProductStatus.values()[random.nextInt(Product.ProductStatus.values().length)]);

            // 사용자 설정
            product.setUser(user);

            productRepository.save(product);
        }
    }
}
