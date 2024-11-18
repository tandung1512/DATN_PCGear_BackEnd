package web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    // Additional custom queries can be added here if needed
	List<Product> findByNameContainingIgnoreCase(String name);
	List<Product> findByIsHotTrue();
}
