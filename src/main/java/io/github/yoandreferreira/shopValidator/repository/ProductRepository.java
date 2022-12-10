package io.github.yoandreferreira.shopValidator.repository;

import io.github.yoandreferreira.shopValidator.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByIdentifier(String identifier);
}
