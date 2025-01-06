package com.market.saessag.product.dto;

import com.market.saessag.user.dto.UserResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
    private Long productId;
    private String photo;
    private String title;
    private Long price;
    private String description;
    private String meetingPlace;
    private String addedDate;
    private String status;
    private UserResponse user;
}
