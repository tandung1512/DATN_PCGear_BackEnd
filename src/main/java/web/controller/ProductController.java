package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import web.model.Product;
import web.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

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
            // Return an error response if the product is not found
            return ResponseEntity.status(e.getStatusCode()).body(new ProductErrorResponse(e.getReason()));
        }
    }

    @Operation(summary = "Add a new product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid product data"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.status(201).body(productService.addProduct(product));
    }

    @Operation(summary = "Update product by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid data provided"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProductById(@PathVariable String id, @RequestBody Product updatedProduct) {
        return ResponseEntity.ok(productService.updateProductById(id, updatedProduct));
    }

    @Operation(summary = "Delete product by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted product"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // Error response class
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
