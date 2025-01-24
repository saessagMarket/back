package com.market.saessag.domain.product.service;

import com.market.saessag.domain.product.dto.ProductRequest;
import com.market.saessag.domain.product.dto.ProductResponse;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.repository.ProductRepository;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.domain.user.dto.UserResponse;
import com.market.saessag.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    //상품 생성
    public ProductResponse createProduct(ProductRequest productRequest) {
        User user = userRepository.findById(productRequest.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Product product = Product.builder()
                .user(user)
                .title(productRequest.getTitle())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .latitude(productRequest.getLatitude())
                .longitude(productRequest.getLongitude())
                .basicAddress(productRequest.getBasicAddress())
                .detailedAddress(productRequest.getDetailedAddress())
                .photo(productRequest.getPhoto())
                .status(Product.ProductStatus.valueOf(productRequest.getStatus()))
                .build();
        return convertToDTO(productRepository.save(product));
    }

    //상품 수정
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("없는 상품 번호 입니다."));

        product.updateProduct(
                productRequest.getTitle(),
                productRequest.getPrice(),
                productRequest.getDescription(),
                productRequest.getPhoto(),
                productRequest.getLatitude(),
                productRequest.getLongitude(),
                productRequest.getBasicAddress(),
                productRequest.getDetailedAddress(),
                Product.ProductStatus.valueOf(productRequest.getStatus()));

        return convertToDTO(productRepository.save(product));
    }

    public boolean deleteProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
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
        User user = product.getUser();

        return ProductResponse.builder()
                .productId(product.getProductId())
                .photo(product.getPhoto())
                .title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())
                .basicAddress(product.getBasicAddress())
                .detailedAddress(product.getDetailedAddress())
                .addedDate(TimeUtils.getRelativeTime(product.getAddedDate()))
                .status(product.getStatus().toString())
                .user(UserResponse.builder()
                        .id(user.getId())
                        .nickname(user.getNickname())
                        .profileUrl(user.getProfileUrl())
                        .build())
                .build();
    }

    public ProductResponse getProductDetail(Long productId) {
        Product id = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("상품이 없습니다."));
        return convertToDTO(id);
    }
}
