package com.market.saessag.domain.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.repository.ProductRepository;
import com.market.saessag.domain.user.entity.User;
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

    Product product = new Product();
    product.setProductId(1L);

    when(productRepository.findByProductIdAndUserId(anyLong(), anyLong()))
        .thenReturn(Optional.of(product));

    // when
    Product newProduct = productService.bumpProduct(product.getProductId(), user.getId());

    // then
    assertNotNull(newProduct);
    assertEquals(product.getProductId(), newProduct.getProductId());
    assertNotNull(newProduct.getBumpAt());
    verify(productRepository).save(newProduct);
  }
}