package web.service;

import web.model.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();  // Method to get all products
    Product getProductById(String id);  // Method to get a product by ID
    Product addProduct(Product product);  // Method to add a new product
    Product updateProductById(String id, Product updatedProduct);  // Method to update product by ID
    void deleteProduct(String id);  // Method to delete a product by ID
}
