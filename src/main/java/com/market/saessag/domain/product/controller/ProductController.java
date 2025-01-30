package com.market.saessag.domain.product.controller;

import com.market.saessag.domain.product.dto.ProductRequest;
import com.market.saessag.domain.product.dto.ProductResponse;
import com.market.saessag.domain.product.service.ProductService;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.global.response.ApiResponse;
import jakarta.servlet.http.HttpSession;
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
    public ApiResponse<Page<ProductResponse>> searchProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String sort, //정렬 기준 (price, addedDate 등)
            @RequestParam(defaultValue = "desc") String direction) {
        Page<ProductResponse> product = productService.searchProducts(page, size, title, nickname, sort, direction);
        return ApiResponse.<Page<ProductResponse>>builder()
                .status("200")
                .data(product)
                .build();
    }

    //상세 조회
    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductDetail(@PathVariable Long productId,
                                                         @SessionAttribute(name = "user", required = false) SignInResponse user) {

        ProductResponse productDetail = productService.getProductDetail(productId);
        if (user != null) {
            productService.incrementView(productId, user.getId());
        }

        return ApiResponse.<ProductResponse>builder()
                .status("200")
                .data(productDetail)
                .build();
    }

    //상품 생성
    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse createdProduct = productService.createProduct(productRequest);
        return ApiResponse.<ProductResponse>builder()
                .status("201")
                .data(createdProduct)
                .build();
    }

    //상품 수정
    @PutMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Long productId,
                                 @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(productId, productRequest);
        return ApiResponse.<ProductResponse>builder()
                .status("200")
                .data(updatedProduct)
                .build();
    }

    //상품 삭제
    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long productId) {
        boolean isDeleted = productService.deleteProduct(productId);
        if (!isDeleted) {
            return ApiResponse.<Void>builder()
                    .status("404")
                    .data(null)
                    .build();
        }

        return ApiResponse.<Void>builder()
                .status("204")
                .data(null)
                .build();
    }

    // 상품 좋아요
    @PostMapping("/{productId}/like")
    public ApiResponse<Void> likeProduct(@PathVariable Long productId, @SessionAttribute(name = "user") SignInResponse user) {
        productService.likeProduct(productId, user.getId());

        return ApiResponse.<Void>builder()
                .status("200")
                .data(null)
                .build();
    }
}
