package com.market.saessag.domain.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.market.saessag.domain.product.dto.ProductResponse;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.repository.ProductLikeRepository;
import com.market.saessag.domain.product.repository.ProductRepository;
import com.market.saessag.domain.product.repository.ProductViewRepository;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
  void bumpProduct() {
    // given
    User user = User.builder()
        .id(1L)
        .email("email")
        .password("pw")
        .nickname("nickname")
        .role("role")
        .build();

    Product product = Product.builder()
            .id(1L)
            .user(user)
            .title("title")
            .price(1000L)
            .description("description")
            .status(Product.ProductStatus.FOR_SALE)
            .build();

    HttpServletRequest httpRequest = mock(HttpServletRequest.class);
    HttpSession session = mock(HttpSession.class);
    SignInResponse userSession = new SignInResponse(user.getId(), user.getEmail(), user.getProfileUrl(), user.getNickname());

    // 모킹 설정
    when(httpRequest.getSession()).thenReturn(session);
    when(session.getAttribute("userProfile")).thenReturn(userSession);
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
    when(productRepository.save(any(Product.class))).thenReturn(product);
    when(productLikeRepository.countByProduct(any(Product.class))).thenReturn(0L);
    when(productViewRepository.countByProduct(any(Product.class))).thenReturn(0L);

    // when
    ProductResponse response = productService.bumpProduct(product.getId(), httpRequest);

    // then
    assertNotNull(response);
    verify(productRepository).save(any(Product.class));
  }
}
