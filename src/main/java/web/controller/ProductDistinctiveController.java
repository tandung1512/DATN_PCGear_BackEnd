package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.ProductDistinctive;
import web.service.ProductDistinctiveService;

import java.util.List;

@RestController
@RequestMapping("/api/product-distinctives")
public class ProductDistinctiveController {

    @Autowired
    private ProductDistinctiveService productDistinctiveService;

    // Get all ProductDistinctives
    @GetMapping
    public ResponseEntity<List<ProductDistinctive>> getAllProductDistinctives() {
        List<ProductDistinctive> productDistinctives = productDistinctiveService.getAllProductDistinctives();
        return ResponseEntity.ok(productDistinctives);
    }

    // Get ProductDistinctive by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDistinctive> getProductDistinctiveById(@PathVariable Long id) {
        ProductDistinctive productDistinctive = productDistinctiveService.getProductDistinctiveById(id);
        return ResponseEntity.ok(productDistinctive);
    }

    // Add a new ProductDistinctive
    @PostMapping
    public ResponseEntity<ProductDistinctive> addProductDistinctive(@RequestBody ProductDistinctive productDistinctive) {
        ProductDistinctive createdProductDistinctive = productDistinctiveService.addProductDistinctive(productDistinctive);
        return ResponseEntity.status(201).body(createdProductDistinctive);
    }

    // Update an existing ProductDistinctive by ID
    @PutMapping("/{id}")
    public ResponseEntity<ProductDistinctive> updateProductDistinctive(@PathVariable Long id, @RequestBody ProductDistinctive updatedProductDistinctive) {
        ProductDistinctive productDistinctive = productDistinctiveService.updateProductDistinctive(id, updatedProductDistinctive);
        return ResponseEntity.ok(productDistinctive);
    }

    // Delete ProductDistinctive by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductDistinctive(@PathVariable Long id) {
        productDistinctiveService.deleteProductDistinctive(id);
        return ResponseEntity.noContent().build();
    }

    // Get ProductDistinctive by Product ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductDistinctive>> getByProductId(@PathVariable String productId) {
        List<ProductDistinctive> productDistinctives = productDistinctiveService.getByProductId(productId);
        return ResponseEntity.ok(productDistinctives);
    }

    // Get ProductDistinctive by Distinctive ID
    @GetMapping("/distinctive/{distinctiveId}")
    public ResponseEntity<List<ProductDistinctive>> getByDistinctiveId(@PathVariable String distinctiveId) {
        List<ProductDistinctive> productDistinctives = productDistinctiveService.getByDistinctiveId(distinctiveId);
        return ResponseEntity.ok(productDistinctives);
    }
}
