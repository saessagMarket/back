package com.market.saessag.product.controller;


import com.market.saessag.product.entity.Product;
import com.market.saessag.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    //전체 상품 조회
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    //전체 상품 조회 (페이지 (기본 최신순 정렬))
    @GetMapping
    public Page<Product> getProducts(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return productService.getAllProductsByPage(page,size);
    }

    //특정 사용자 상품 조회
    @GetMapping("/user/{userId}")
    public List<Product> getProductsByUserId(@PathVariable Long userId) {
        return productService.getProductsByUserId(userId);
    }

    //상품 이름 조회
    @GetMapping("/{title}")
    public List<Product> getProductsByTitle(@PathVariable String title) {
        return productService.getProductsByTitle(title);
    }

    //가격순 정렬
    @GetMapping("/sorted-by-price")
    public Page<Product> getProductsSortByPrice(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "asc") String direction) {
        return productService.getProductsSortByPrice(page, size,direction);
    }

    //상품 생성
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

}
