package catalog_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import catalog_service.model.Product;

@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {}