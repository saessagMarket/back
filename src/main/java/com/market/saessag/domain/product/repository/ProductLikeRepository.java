package com.market.saessag.domain.product.repository;

import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.entity.ProductLike;
import com.market.saessag.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    ProductLike findByProductAndUser(Product product, User user);

    Long countByProduct(Product product);
}
