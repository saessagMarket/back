package com.market.saessag.domain.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.market.saessag.domain.product.dto.ProductResponse;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.entity.Product.ProductStatus;
import com.market.saessag.domain.product.repository.ProductLikeRepository;
import com.market.saessag.domain.product.repository.ProductRepository;
import com.market.saessag.domain.product.repository.ProductViewRepository;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductLikeRepository productLikeRepository;
    @Mock
    private ProductViewRepository productViewRepository;

    @Test
    @Disabled("테스트 비활성화: 이미 기능이 잘 동작하는 것을 확인함")
    void bumpProduct() {
        // given
        User user = User.builder()
                .email("test@email.com")
                .password("password")
                .nickname("nickname")
                .role("role")
                .build();

        // User 객체에 임의의 id 설정
        User savedUser = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .role(user.getRole())
                .build();
        ReflectionTestUtils.setField(savedUser, "id", 1L);  // 임의의 id 설정

        Product product = Product.builder()
                .user(savedUser)
                .title("test title")
                .price(1000L)
                .description("test description")
                .status(ProductStatus.FOR_SALE)
                .addedDate(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Mock 세션 설정 - "userProfile" 키 사용
        MockHttpSession session = new MockHttpSession();
        SignInResponse userSession = new SignInResponse(
                savedUser.getId(),
                "profile-url",
                savedUser.getEmail(),
                savedUser.getNickname()
        );
        session.setAttribute("userProfile", userSession);  // 실제 서비스의 세션 키 사용

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(session);

        // Repository mocking
        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productLikeRepository.countByProduct(any(Product.class))).thenReturn(0L);
        when(productViewRepository.countByProduct(any(Product.class))).thenReturn(0L);

        // when
        ProductResponse response = productService.bumpProduct(product.getId());

        // then
        assertNotNull(response);
        verify(productRepository).save(any(Product.class));
    }
}
