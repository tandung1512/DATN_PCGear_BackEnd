package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.Product;
import web.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {

    // Lấy tất cả sản phẩm
    List<Product> getAllProducts();  // Method to get all products

    // Lấy sản phẩm theo ID
    Product getProductById(String id);  // Method to get a product by ID

    // Thêm một sản phẩm mới
    Product addProduct(Product product);  // Method to add a new product

    // Cập nhật sản phẩm theo ID
    Product updateProductById(String id, Product updatedProduct);  // Method to update product by ID

    // Xóa sản phẩm theo ID
    void deleteProduct(String id);  // Method to delete a product by ID
    
    public List<Product> searchProductsByName(String name);
}

