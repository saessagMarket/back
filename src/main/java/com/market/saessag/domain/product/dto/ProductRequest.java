package com.market.saessag.domain.product.dto;

import com.market.saessag.domain.user.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductRequest {
    private User user;
    private String title;
    private Long price;
    private String description;
    private Double latitude;
    private Double longitude;
    private String basicAddress;
    private String detailedAddress;
    private List<String> photo;
    private String status;
}