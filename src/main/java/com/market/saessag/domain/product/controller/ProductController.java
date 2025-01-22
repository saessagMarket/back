package com.market.saessag.domain.product.controller;

import com.market.saessag.domain.product.dto.ProductRequest;
import com.market.saessag.domain.product.dto.ProductResponse;
import com.market.saessag.domain.product.service.ProductService;
import com.market.saessag.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    //통합 조회 (제목, 닉네임, 정렬기준)
    @GetMapping()
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String sort, //정렬 기준 (price, addedDate 등)
            @RequestParam(defaultValue = "desc") String direction) {
        Page<ProductResponse> product = productService.searchProducts(page, size, title, nickname, sort, direction);
        ApiResponse<Page<ProductResponse>> response = ApiResponse.<Page<ProductResponse>>builder()
                .status("200")
                .data(product)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetail(@PathVariable Long productId) {
        ProductResponse productDetail = productService.getProductDetail(productId);
        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .status("200")
                .data(productDetail)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //상품 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse createdProduct = productService.createProduct(productRequest);
        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .status("201")
                .data(createdProduct)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long productId,
                                 @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(productId, productRequest);
        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .status("200")
                .data(updatedProduct)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        boolean isDeleted = productService.deleteProduct(productId);
        if (!isDeleted) {
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .status("404")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("204")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
