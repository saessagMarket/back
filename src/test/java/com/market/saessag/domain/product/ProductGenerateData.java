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
            Product product = Product.builder()
                    .title("상품명 " + i)
                    .price((long) (1000 + random.nextInt(9000)))
                    .description("상품의 설명 " + i)
                    .latitude(37.5 + random.nextDouble())
                    .longitude(127.0 + random.nextDouble())
                    .basicAddress("도로명 주소 " + (random.nextInt(10) + 1))
                    .detailedAddress("상세 주소 " + (random.nextInt(100)) + 1)
                    .photo(List.of("photo1.jpg", "photo2.jpg", "photo3.jpg"))
                    .status(Product.ProductStatus.values()[random.nextInt(Product.ProductStatus.values().length)])
                    .user(user)
                    .build();

            productRepository.save(product);
        }
    }
}
