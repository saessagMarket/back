package com.market.saessag.domain.product.controller;

import com.market.saessag.domain.product.dto.ProductResponse;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    //통합 조회 (제목, 닉네임, 정렬기준)
    @GetMapping()
    public Page<ProductResponse> searchProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String sort, //정렬 기준 (price, addedDate 등)
            @RequestParam(defaultValue = "desc") String direction) {

        return productService.searchProducts(page, size, title, nickname, sort, direction);
    }

    //상세 조회
    @GetMapping("/{productId}")
    public ProductResponse getProductDetail(@PathVariable Long productId) {
        return productService.getProductDetail(productId);
    }

    //상품 생성
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    //상품 수정
    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable Long productId,
                                 @RequestBody Product product) {
        return productService.updateProduct(productId, product);
    }

    //상품 삭제
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }
}
