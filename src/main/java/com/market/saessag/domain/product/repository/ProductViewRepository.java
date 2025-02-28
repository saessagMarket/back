package com.market.saessag.domain.product.repository;

import com.market.saessag.domain.product.entity.Product;
import com.market.saessag.domain.product.entity.ProductView;
import com.market.saessag.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductViewRepository extends JpaRepository<ProductView, Long> {
    boolean existsByProductAndUser(Product product, User user);

    Long countByProduct(Product product);
}