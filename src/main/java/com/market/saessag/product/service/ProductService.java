package com.market.saessag.product.service;

import com.market.saessag.product.entity.Product;
import com.market.saessag.product.repository.ProductRepository;
import com.market.saessag.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    //전체 상품 조회
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //전체 상품 조회 (페이지)
    public Page<Product> getAllProductsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("addedDate").descending());
        return productRepository.findAll(pageable);
    }

    //특정 사용자 상품 조회
    public List<Product> getProductsByNickname(String nickname) {
        Long userId = userRepository.findByNickname(nickname).getId();
        return productRepository.findByUserId(userId);
    }

    //상품 이름 조회
    public List<Product> getProductsByTitle(String title) {
        return productRepository.findByTitle(title);
    }

    //가격순 정렬
    public Page<Product> getProductsSortByPrice(int page, int size, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by("price").ascending()
                : Sort.by("price").descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable);
    }

    //상품 생성
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    //상품 수정 폼
    public Optional<Product> updateProductForm(Long productId) {
        return productRepository.findById(productId);
    }

    //상품 수정
    public Product updateProduct(Long productId, Product product) {
        //수정 시 추가된 날짜 업데이트 X ?
        return productRepository.findById(productId)
                .map(afterProduct -> {
                    afterProduct.setDescription(product.getDescription());
                    afterProduct.setMeetingPlace(product.getMeetingPlace());
                    afterProduct.setPhoto(product.getPhoto());
                    afterProduct.setPrice(product.getPrice());
                    afterProduct.setStatus(product.getStatus());
                    afterProduct.setTitle(product.getTitle());
                    return productRepository.save(afterProduct);
                }).orElseThrow(() -> new IllegalArgumentException("없는 상품 번호 입니다."));
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
