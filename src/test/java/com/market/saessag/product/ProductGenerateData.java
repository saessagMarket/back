package com.market.saessag.product;

import com.market.saessag.product.entity.Product;
import com.market.saessag.product.repository.ProductRepository;
import com.market.saessag.user.entity.User;
import com.market.saessag.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Random;

@SpringBootTest
public class ProductGenerateData {
    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private UserRepository userRepository; // UserRepository 추가
    @Test
    public void 상품_데이터_생성() {
        Random random = new Random();

        // 임의의 User 생성 및 저장
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("0000");
        user.setNickname("테스트닉네임");
        userRepository.save(user); // User 먼저 저장

        for (int i = 1; i <= 100; i++) {
            Product product = new Product();
            product.setPhoto("photo" + i + ".jpg");
            product.setTitle("상품명 " + i);
            product.setPrice((long) (1000 + random.nextInt(9000))); // 1000 ~ 10000 사이의 값
            product.setDescription("상품의 설명 " + i);
            product.setMeetingPlace("만날 장소 " + (random.nextInt(10) + 1)); // City 1 ~ City 10
            product.setAddedDate(LocalDate.now().minusDays(random.nextInt(30))); // 최근 30일 내 날짜
            product.setStatus(Product.ProductStatus.values()[random.nextInt(Product.ProductStatus.values().length)]); // Enum 랜덤 선택
            product.setUser(user);

            productRepository.save(product);
        }
    }
}
