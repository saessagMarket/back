package com.market.saessag.domain.product.service;

import com.market.saessag.domain.product.dto.ProductChangeStatusRequest;
import com.market.saessag.domain.product.dto.ProductChangeStatusResponse;
import com.market.saessag.domain.product.dto.ProductRequest;
import com.market.saessag.domain.product.dto.ProductResponse;
import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.entity.ProductLike;
import com.market.saessag.domain.product.entity.ProductView;
import com.market.saessag.domain.product.repository.ProductLikeRepository;
import com.market.saessag.domain.product.repository.ProductRepository;
import com.market.saessag.domain.product.repository.ProductViewRepository;
import com.market.saessag.domain.user.dto.SignInResponse;
import com.market.saessag.domain.user.entity.User;
import com.market.saessag.domain.user.repository.UserRepository;
import com.market.saessag.domain.user.dto.UserProfileResponse;
import com.market.saessag.global.exception.CustomException;
import com.market.saessag.global.exception.ErrorCode;
import com.market.saessag.global.util.SessionUtils;
import com.market.saessag.util.TimeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductViewRepository productViewRepository;
    private final UserRepository userRepository;
    private final ProductLikeRepository productLikeRepository;

    // 상품 가져오기
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    // 상품 생성
    public ProductResponse createProduct(ProductRequest request) {
        User user = getUserFromSession();
        Product product = Product.createProduct(user, request);
        return convertToDTO(productRepository.save(product));
    }

    // 상품 수정
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        User user = getUserFromSession();
        Product product = getProductAndValidateOwner(productId, user.getId());

        product.updateProduct(request);
        return convertToDTO(productRepository.save(product));
    }

    // 상품 삭제
    public boolean deleteProduct(Long productId) {
        User user = getUserFromSession();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("없는 상품 번호 입니다."));

        // 글쓴이와 현재 로그인한 사용자가 같은지 확인
        if (!product.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        try {
            productRepository.deleteById(productId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 상품 검색, 필터링, 정렬
    public Page<ProductResponse> searchProducts(int page, int size, String title, String nickname, String sort) {
        try {
            Sort sorting = (sort == null || sort.isEmpty()) ?
                Sort.by(
                    Sort.Order.desc("updatedAt")  // 최신 업데이트 순
                ) : Sort.by(Sort.Order.by(sort));

            Pageable pageable = PageRequest.of(page, size, sorting);

            Page<ProductResponse> result;
            if (title != null) {
                result = productRepository.findByTitleContaining(title, pageable).map(this::convertToDTO);
            } else if (nickname != null) {
                User user = userRepository.findByNickname(nickname);
                if (user == null) {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }
                result = productRepository.findByUser(user, pageable).map(this::convertToDTO);
            } else {
                result = productRepository.findAll(pageable).map(this::convertToDTO);
            }
            return result;
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ARGUMENT);
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
                .like(productLikeRepository.countByProduct(product))
                .view(productViewRepository.countByProduct(product))
                .user(UserProfileResponse.builder()
                        .id(user.getId())
                        .nickname(user.getNickname())
                        .profileUrl(user.getProfileUrl())
                        .build())
                .build();
    }

    // 세션에서 사용자 정보를 가져와서 검증
    private User getUserFromSession() {
        SignInResponse userSession = SessionUtils.getUserSession();

        return userRepository.findById(userSession.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    // 상품을 찾고 해당 상품의 소유자가 맞는지 검증
    private Product getProductAndValidateOwner(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!product.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return product;
    }

    public ProductResponse getProductDetail(Long productId) {
        Product id = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("상품이 없습니다."));
        return convertToDTO(id);
    }

    // 상품 끌어올리기
    public ProductResponse bumpProduct(Long productId) {
        User user = getUserFromSession();
        Product product = getProductAndValidateOwner(productId, user.getId());

        product.bump();  // updatedAt만 갱신
        return convertToDTO(productRepository.save(product));
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
        }
    }

    // 좋아요 클릭
    @Transactional
    public boolean likeProduct(Long productId, Long userId) {
        // 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 좋아요 상태 확인
        ProductLike productLike = productLikeRepository.findByProductAndUser(product, user);

        if (productLike == null) {
            productLikeRepository.save(ProductLike.builder()
                    .product(product)
                    .user(user)
                    .build());
            return true;  // 좋아요 추가됨
        } else {
            productLikeRepository.delete(productLike);
            return false;  // 좋아요 취소됨
        }
    }

    // 상품 상태 값 변경
    public ProductChangeStatusResponse changeStatus(ProductChangeStatusRequest request) {
        User user = getUserFromSession();
        Product product = getProductAndValidateOwner(request.getId(), user.getId());

        try {
            product.updateStatus(request.getStatus());
            Product savedProduct = productRepository.save(product);

            return ProductChangeStatusResponse.builder()
                    .id(savedProduct.getId())
                    .status(savedProduct.getStatus())
                    .build();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
