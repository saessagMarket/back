package com.market.saessag.domain.product.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
