package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.ProductDistinctive;
import web.repository.ProductDistinctiveRepository;
import java.util.List;

@Service
public class ProductDistinctiveService {

    @Autowired
    private ProductDistinctiveRepository productDistinctiveRepository;

    // Get all ProductDistinctives
    public List<ProductDistinctive> getAllProductDistinctives() {
        return productDistinctiveRepository.findAll();
    }

    // Get ProductDistinctive by ID
    public ProductDistinctive getProductDistinctiveById(Long id) {
        return productDistinctiveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductDistinctive not found with ID: " + id));
    }

    // Add a new ProductDistinctive
    public ProductDistinctive addProductDistinctive(ProductDistinctive productDistinctive) {
        return productDistinctiveRepository.save(productDistinctive);
    }

    // Update an existing ProductDistinctive by ID
    public ProductDistinctive updateProductDistinctive(Long id, ProductDistinctive updatedProductDistinctive) {
        ProductDistinctive existingProductDistinctive = getProductDistinctiveById(id);
        existingProductDistinctive.setProduct(updatedProductDistinctive.getProduct());
        existingProductDistinctive.setDistinctive(updatedProductDistinctive.getDistinctive());
        return productDistinctiveRepository.save(existingProductDistinctive);
    }

    // Delete ProductDistinctive by ID
    public void deleteProductDistinctive(Long id) {
        productDistinctiveRepository.deleteById(id);
    }

    // Get ProductDistinctive by Product ID
    public List<ProductDistinctive> getByProductId(String productId) {
        return productDistinctiveRepository.findByProductId(productId);
    }

    // Get ProductDistinctive by Distinctive ID
    public List<ProductDistinctive> getByDistinctiveId(String distinctiveId) {
        return productDistinctiveRepository.findByDistinctiveId(distinctiveId);
    }
}
