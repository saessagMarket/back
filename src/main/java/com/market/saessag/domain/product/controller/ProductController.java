package com.market.saessag.domain.product.controller;

import com.market.saessag.domain.product.dto.ProductChangeStatusRequest;
import com.market.saessag.domain.product.dto.ProductChangeStatusResponse;
import com.market.saessag.domain.product.dto.ProductRequest;
import com.market.saessag.domain.product.dto.ProductResponse;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.service.ProductService;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
            @RequestParam(required = false) String sort
    ) {
        // 세션 체크는 인터셉터에서 처리되므로, 여기서는 비즈니스 로직만 처리
        Page<ProductResponse> product = productService.searchProducts(page, size, title, nickname, sort);

        // success() 메서드를 사용하여 일관된 응답 형식 유지
        return ApiResponse.success(SuccessCode.OK, product);
    }

    //상세 조회
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductDetail(@PathVariable Long id,
                                                         @SessionAttribute(name = "user", required = false) SignInResponse user) {

        ProductResponse productDetail = productService.getProductDetail(id);
        if (user != null) {
            productService.incrementView(id, user.getId());
        }
        return ApiResponse.success(SuccessCode.OK, productDetail);
    }

    //상품 생성
    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse createdProduct = productService.createProduct(productRequest);
        return ApiResponse.success(SuccessCode.PRODUCT_CREATED, createdProduct);
    }

    //상품 수정
    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Long id,
                                 @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        return ApiResponse.success(SuccessCode.OK, updatedProduct);
    }

    //상품 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productService.deleteProduct(id);
        if (!isDeleted) {
            return ApiResponse.error(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return ApiResponse.success(SuccessCode.NO_CONTENT, null);
    }

    // 상품 좋아요
    @PostMapping("/{id}/like")
    public ApiResponse<Void> likeProduct(@PathVariable Long id, @SessionAttribute(name = "user") SignInResponse user) {
        productService.likeProduct(id, user.getId());
        return ApiResponse.success(SuccessCode.OK, null);
    }

    @PostMapping("/bump")
    public ApiResponse<?> bumpProduct(@RequestParam Long productId, @SessionAttribute(name = "user") SignInResponse user) {
        Product product = productService.bumpProduct(productId, user.getId());
        return ApiResponse.success(SuccessCode.OK, product.getId());
    }


    // note. 본인 소유의 상품의 상태 값만 변경 할 수 있도록 조치 필요
    @PostMapping("/changeStatus")
    public ApiResponse<ProductChangeStatusResponse> changeStatus(@RequestBody ProductChangeStatusRequest req) {
        ProductChangeStatusResponse response = productService.changeStatus(req);
        return ApiResponse.success(SuccessCode.OK, response);
    }
}
