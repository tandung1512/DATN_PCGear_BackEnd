package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import web.model.*;
import web.repository.ProductDistinctiveRepository;
import web.repository.ProductRepository;
import web.service.*;


import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductRepository productRepository; 
    
    @Autowired
    private ProductDistinctiveRepository productDistinctiveRepository;

    @Autowired
    private CategoryService categoryService; // Inject CategoryService

    @Autowired
    private DistinctiveService distinctiveService; // Inject DistinctiveService

    
    // Get all products
    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(products); // Return 204 if no products found
        }
        return ResponseEntity.ok(products);
    }

    // Get product by ID
    @Operation(summary = "Get product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        try {
            Product product = productService.getProductById(id); // Retrieve the product by ID
            return ResponseEntity.ok(product); // Return 200 if product found
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ProductErrorResponse(e.getReason()));
        }
    }

    // Add a new product
    @Operation(summary = "Add a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestParam String id,
                                              @RequestParam String name,
                                              @RequestParam int quantity,
                                              @RequestParam double price,
                                              @RequestParam String description,
                                              @RequestParam String status,
                                              @RequestParam String categoryId,  // Use categoryId instead of category name
                                              @RequestParam List<String> distinctiveIds,
                                              @RequestParam(value = "image1", required = false) MultipartFile image1,
                                              @RequestParam(value = "image2", required = false) MultipartFile image2) {

        // Validate ID
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Return 400 if ID is invalid
        }

        // Convert images to Base64 if they are provided
        String image1Base64 = image1 != null ? convertFileToBase64(image1) : null;
        String image2Base64 = image2 != null ? convertFileToBase64(image2) : null;

        // Get Category by ID
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // If Category not found, return error
        }

        // Get Distinctives by IDs
        List<Distinctive> productDistinctives = distinctiveService.getDistinctivesByIds(distinctiveIds);

        // Create new Product object
        Product product = new Product(id, name, quantity, price, description, status, image1Base64, image2Base64, category, productDistinctives);

        // Save Product to the database
        Product addedProduct = productService.addProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }

    // Update product by ID
    @Operation(summary = "Update product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProductById(@PathVariable String id,
                                                     @RequestParam String name,
                                                     @RequestParam int quantity,
                                                     @RequestParam double price,
                                                     @RequestParam String description,
                                                     @RequestParam String status,
                                                     @RequestParam String categoryId,  // Use categoryId instead of category name
                                                     @RequestParam List<String> distinctiveIds,
                                                     @RequestParam(value = "image1", required = false) MultipartFile image1,
                                                     @RequestParam(value = "image2", required = false) MultipartFile image2) {

        // Validate ID
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Return 400 if ID is invalid
        }

        // Convert images to Base64 if provided
        String image1Base64 = image1 != null ? convertFileToBase64(image1) : null;
        String image2Base64 = image2 != null ? convertFileToBase64(image2) : null;

        // Get Category by ID
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Return error if Category not found
        }

        // Get Distinctives by IDs
        List<Distinctive> productDistinctives = distinctiveService.getDistinctivesByIds(distinctiveIds);

        // Create updated Product object
        Product updatedProduct = new Product(id, name, quantity, price, description, status, image1Base64, image2Base64, category, productDistinctives);

        // Update Product in the database
        Product resultProduct = productService.updateProductById(id, updatedProduct);

        return ResponseEntity.ok(resultProduct);
    }

    // Convert MultipartFile to Base64 string
    private String convertFileToBase64(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
 // Xóa sản phẩm và các liên kết trong bảng products_distinctives
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        try {
            // Kiểm tra xem sản phẩm có tồn tại hay không
            Optional<Product> productOptional = productRepository.findById(id);
            if (!productOptional.isPresent()) {
                return ResponseEntity.status(404).body("Product not found");
            }

            // Lấy sản phẩm
            Product product = productOptional.get();

          

            // Xóa sản phẩm
            productRepository.deleteById(id);

            return ResponseEntity.ok("Product and related records deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting product: " + e.getMessage());
        }
    }



    // Error response class for Product
    public static class ProductErrorResponse {
        private String message;

        public ProductErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
