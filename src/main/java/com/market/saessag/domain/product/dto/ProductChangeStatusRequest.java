package com.market.saessag.domain.product.dto;

import com.market.saessag.domain.product.entity.Product.ProductStatus;
import lombok.Getter;

@Getter
public class ProductChangeStatusRequest {

  private Long productId;
  private ProductStatus status;

}
