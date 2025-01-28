package com.market.saessag.domain.product.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductRequest {
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