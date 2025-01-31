package com.market.saessag.domain.product.service;

import com.market.saessag.domain.product.dto.ProductRequest;
import com.market.saessag.domain.product.dto.ProductResponse;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.entity.ProductLike;
import com.market.saessag.domain.product.entity.ProductView;
import com.market.saessag.domain.product.repository.ProductLikeRepository;
import com.market.saessag.domain.product.repository.ProductRepository;
import com.market.saessag.domain.product.repository.ProductViewRepository;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.domain.user.dto.UserResponse;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.util.TimeUtils;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
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
    private final ProductViewRepository productViewRepository;
    private final UserRepository userRepository;
    private final ProductLikeRepository productLikeRepository;

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

    public Page<ProductResponse> searchProducts(int page, int size, String title, String nickname, String sort) {
        try {
            Sort sorting = (sort == null || sort.isEmpty()) ?
                    Sort.by(
                            Sort.Order.desc("dump_at").nullsLast(),
                            Sort.Order.desc("added_date")
                    ) : Sort.by(Sort.Order.by(sort));

            Pageable pageable = PageRequest.of(page, size, sorting);

            if (title != null) {
                return productRepository.findByTitleContaining(title, pageable).map(this::convertToDTO);
            } else if (nickname != null) {
                User user = userRepository.findByNickname(nickname);
                if (user == null) {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }
                return productRepository.findByUser(user, pageable).map(this::convertToDTO);
            } else {
                return productRepository.findAll(pageable).map(this::convertToDTO);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private ProductResponse convertToDTO(Product product) {
        User user = product.getUser();

        return ProductResponse.builder()
                .productId(product.getId())
                .photo(product.getPhoto())
                .title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())
                .basicAddress(product.getBasicAddress())
                .detailedAddress(product.getDetailedAddress())
                .addedDate(TimeUtils.getRelativeTime(product.getAddedDate()))
                .status(product.getStatus().toString())
                .like(product.getLikes())
                .view(product.getViews())
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

    public Product bumpProduct(Long productId, Long userId) {
        Product product = productRepository.findByIdAndUserId(productId, userId)
            .orElseThrow(IllegalAccessError::new);

        product.updateBumpAt(LocalDateTime.now());
        productRepository.save(product);
        return product;
    }

    // 조회수 증가
    public void incrementView(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        boolean hasViewed = productViewRepository.existsByProductAndUser(product, user);
        if (!hasViewed) {
            ProductView productView = ProductView.builder()
                    .product(product)
                    .user(user)
                    .build();
            productViewRepository.save(productView);

            product.incrementViews();
            productRepository.save(product);
        }

    }

    // 좋아요 클릭
    @Transactional
    public void likeProduct(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        ProductLike productLike = productLikeRepository.findByProductAndUser(product, user);
        if (productLike == null) { // 좋아요 추가
            productLikeRepository.save(ProductLike.builder()
                    .product(product)
                    .user(user)
                    .build());
            product.incrementLikes();
        } else { // 좋아요 삭제
            productLikeRepository.delete(productLike);
            product.decrementLikes();
        }
        productRepository.save(product);
    }
}
