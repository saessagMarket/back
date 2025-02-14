package com.market.saessag.domain.product.controller;

import com.market.saessag.domain.product.dto.ProductChangeStatusRequest;
import com.market.saessag.domain.product.dto.ProductChangeStatusResponse;
import com.market.saessag.domain.product.dto.ProductRequest;
import com.market.saessag.domain.product.dto.ProductResponse;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.service.ProductService;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.response.ApiResponse;
import com.market.saessag.global.response.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 세션 동작 방식
 * 1. 사용자가 요청을 보내면 Spring Security 필터체인이 동작
 * 2. SecurityContextPersistenceFilter가 세션에서 인증 정보를 찾아 SecurityContextHolder에 설정
 * 3. 컨트롤러에서 세션 정보 확인
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    //통합 조회 (제목, 닉네임, 정렬기준)
    @GetMapping("/list")
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
    public ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest productRequest,
                                                      HttpServletRequest request) {
        ProductResponse createdProduct = productService.createProduct(productRequest, request);
        return ApiResponse.success(SuccessCode.PRODUCT_CREATED, createdProduct);
    }

    //상품 수정
    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest productRequest,
            HttpServletRequest request) {
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest, request);
        return ApiResponse.success(SuccessCode.OK, updatedProduct);
    }

    //상품 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(
            @PathVariable Long id,
            HttpServletRequest request) {
        boolean isDeleted = productService.deleteProduct(id, request);
        if (!isDeleted) {
            return ApiResponse.error(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return ApiResponse.success(SuccessCode.NO_CONTENT, null);
    }

    // 상품 좋아요
    @PostMapping("/{id}/like")
    public ApiResponse<Void> likeProduct(@PathVariable Long id, HttpServletRequest request) {

        HttpSession session = request.getSession();
        SignInResponse userSession = (SignInResponse) session.getAttribute("userProfile");

        if (userSession == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        boolean isLiked = productService.likeProduct(id, userSession.getId());

        return ApiResponse.success(
                isLiked ? SuccessCode.LIKE_ADDED : SuccessCode.LIKE_REMOVED,
                null
        );
    }

    // 상품 끌어올리기
    // @PreAuthorize("isAuthenticated()")  인증된 사용자만 접근 가능한 시큐리티의 메서드 방식 --> 나중에 리팩토링
    @PostMapping("/bump/{id}")
    public ApiResponse<?> bumpProduct(@PathVariable Long id, HttpServletRequest request) {
        // 1. 세션 확인
        HttpSession session = request.getSession();
        SignInResponse userSession = (SignInResponse) session.getAttribute("userProfile");

        if (userSession == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 2. 서비스 호출 (소유자 검증은 서비스에서 처리)
        Product bumpedProduct = productService.bumpProduct(id, userSession.getId());
        return ApiResponse.success(SuccessCode.OK, bumpedProduct.getId());
    }

    // 상품 상태 값 변경(본인 소유의 상품의 상태 값만 변경 가능)
    @PostMapping("/changeStatus")
    public ApiResponse<ProductChangeStatusResponse> changeStatus(
            @RequestBody ProductChangeStatusRequest req,
            HttpServletRequest request) {
        ProductChangeStatusResponse response = productService.changeStatus(req, request);
        return ApiResponse.success(SuccessCode.OK, response);
    }

}
