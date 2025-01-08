package com.market.saessag.domain.product.repository;

import com.market.saessag.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserId(Long userId);

    List<Product> findByTitle(String title);

    Page<Product> findAll(Pageable pageable);
}
