package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    // Additional custom queries can be added here if needed
}
