package com.market.saessag.domain.product.dto;

import com.market.saessag.domain.user.dto.UserResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ProductResponse {
    private final Long productId;
    private String photo;
    private String title;
    private Long price;
    private String description;
    private String meetingPlace;
    private final String addedDate;
    private String status;
    private final UserResponse user;
}
