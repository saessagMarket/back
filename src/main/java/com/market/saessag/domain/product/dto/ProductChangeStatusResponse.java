package com.market.saessag.domain.product.dto;

import com.market.saessag.domain.product.entity.Product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductChangeStatusResponse {
  private Long id;
  private ProductStatus status;
}
