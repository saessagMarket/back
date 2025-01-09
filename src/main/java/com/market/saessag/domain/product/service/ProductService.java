package com.market.saessag.domain.product.service;

import com.market.saessag.domain.product.dto.ProductResponse;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.repository.ProductRepository;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.domain.user.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    //상품 생성
    public Product createProduct(Product product) {
        return productRepository.save(product);
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

    public Page<ProductResponse> searchProducts(int page, int size, String title, String nickname, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort != null ? sort : "addedDate"));

        if (title != null) {
            return productRepository.findByTitleContaining(title, pageable).map(this::convertToDTO);
        } else if (nickname != null) {
            User user = userRepository.findByNickname(nickname);
            return productRepository.findByUser(user, pageable).map(this::convertToDTO);
        } else {
            return productRepository.findAll(pageable).map(this::convertToDTO);
        }
    }

    private ProductResponse convertToDTO(Product product) {
        //UserResponse 변환
        UserResponse userDTO = new UserResponse();
        userDTO.setId(product.getUser().getId());
        userDTO.setNickname(product.getUser().getNickname());
        userDTO.setProfileUrl(product.getUser().getProfileUrl());

        //Product 정보 DTO로 변환
        ProductResponse productDTO = new ProductResponse();
        productDTO.setProductId(product.getProductId());
        productDTO.setPhoto(product.getPhoto());
        productDTO.setTitle(product.getTitle());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setMeetingPlace(product.getMeetingPlace());
        productDTO.setAddedDate(product.getAddedDate().toString());
        productDTO.setStatus(product.getStatus().toString());
        productDTO.setUser(userDTO);

        return productDTO;
    }
}
