package com.market.saessag.product.repository;

import com.market.saessag.product.entity.Product;
import com.market.saessag.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    Page<Product> findByTitleContaining(String title, Pageable pageable);

    Page<Product> findByUser(User user, Pageable pageable);
}
