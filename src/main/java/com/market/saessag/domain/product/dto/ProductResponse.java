package com.market.saessag.domain.product.dto;

import com.market.saessag.domain.user.dto.UserResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductResponse {
    private final Long productId;
    private List<String> photo;
    private String title;
    private Long price;
    private String description;
    private String basicAddress;
    private String detailedAddress;
    private final String addedDate;
    private String status;
    private Long like;
    private Long view;
    private final UserResponse user;
}
