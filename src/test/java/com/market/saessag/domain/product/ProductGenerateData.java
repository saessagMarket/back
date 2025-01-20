package com.market.saessag.product;

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
                .email("test11@gmail.com")
                .password("0000")
                .profileUrl("photo11.jpg")
                .nickname("테스트 닉네임11")
                .emailVerified(true)     // 이메일 인증 상태를 true로 설정
                .build();
        userRepository.save(user);

        for (int i = 1; i <= 100; i++) {
            Product product = new Product();
            product.setTitle("상품명 " + i);
            product.setPrice((long) (1000 + random.nextInt(9000))); // 1000 ~ 10000 사이의 값
            product.setDescription("상품의 설명 " + i);
            product.setMeetingPlace("만날 장소 " + (random.nextInt(10) + 1)); // City 1 ~ City 10
            product.setStatus(Product.ProductStatus.values()[random.nextInt(Product.ProductStatus.values().length)]); // Enum 랜덤 선택
            product.setUser(user);

            productRepository.save(product);
        }
    }
}
