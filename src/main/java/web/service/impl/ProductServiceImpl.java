package web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import web.model.Product;
import web.repository.ProductRepository;
import web.service.ProductService;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {  // Use 'implements' instead of 'extends'

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();  // Retrieve all products from the repository
    }

    @Override
    public Product getProductById(String id) {
        // Fetch the product by ID. If not found, throw an exception
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id);
        }
        return product.get();  // Return the found product
    }

    @Override
    public Product addProduct(Product product) {
        // Validation checks before adding a product
        if (product.getId() == null || product.getId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product ID is required.");
        }
        if (productRepository.existsById(product.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product with ID " + product.getId() + " already exists.");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required.");
        }
        if (product.getPrice() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product price cannot be negative.");
        }

        return productRepository.save(product);  // Save the new product in the repository
    }

    @Override
    public Product updateProductById(String id, Product updatedProduct) {
        // Fetch the product by ID. If not found, throw an exception
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id));

        // Update the existing product with the provided fields
        if (updatedProduct.getName() != null) {
            existingProduct.setName(updatedProduct.getName());
        }
        if (updatedProduct.getQuantity() >= 0) {
            existingProduct.setQuantity(updatedProduct.getQuantity());
        }
        if (updatedProduct.getPrice() >= 0) {
            existingProduct.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getDescription() != null) {
            existingProduct.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getStatus() != null) {
            existingProduct.setStatus(updatedProduct.getStatus());
        }
        if (updatedProduct.getImage1() != null) {
            existingProduct.setImage1(updatedProduct.getImage1());
        }
        if (updatedProduct.getImage2() != null) {
            existingProduct.setImage2(updatedProduct.getImage2());
        }
        if (updatedProduct.getCategory() != null) {
            existingProduct.setCategory(updatedProduct.getCategory());
        }

        return productRepository.save(existingProduct);  // Save the updated product
    }

    @Override
    public void deleteProduct(String id) {
        // Check if the product exists. If not, throw an exception
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id);
        }
        productRepository.deleteById(id);  // Delete the product from the repository
    }
}
