package com.market.saessag.domain.product.dto;

import com.market.saessag.domain.product.entity.Product.ProductStatus;
import lombok.Builder;

@Builder
public class ProductChangeStatusResponse {

  private Long productId;
  private ProductStatus status;
}
