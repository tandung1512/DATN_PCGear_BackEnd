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

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.MediaType;

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

    
    private static final String IMAGE_DIR = "src/main/resources/webapp/files/images/"; // thư mục lưu ảnh

    // API trả về hình ảnh
    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        // Đảm bảo rằng đường dẫn file ảnh hợp lệ
        Path imagePath = Paths.get("src/main/resources/webapp/files/images/").resolve(imageName);
        Resource resource = new FileSystemResource(imagePath);

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)  // Hoặc MediaType.IMAGE_PNG nếu là ảnh PNG
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();  // Trả về 404 nếu không tìm thấy ảnh
        }
    }
    
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

    @GetMapping("/search/item")
    public List<Product> searchProducts(@RequestParam String name) {
        // Tìm kiếm các sản phẩm có tên chứa từ khóa
        return productRepository.findByNameContainingIgnoreCase(name);
    }
    // Get product by ID
    @Operation(summary = "Get product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/get/{id}")
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
                                              @RequestParam boolean isHot,
                                              @RequestParam String categoryId,
                                              @RequestParam List<String> distinctiveIds,
                                              @RequestParam(value = "image1", required = false) MultipartFile image1,
                                              @RequestParam(value = "image2", required = false) MultipartFile image2) {

    	System.out.println("isHot value: " + isHot);
    	if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String image1Path = image1 != null ? saveFile(image1) : null;
        String image2Path = image2 != null ? saveFile(image2) : null;

        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<Distinctive> productDistinctives = distinctiveService.getDistinctivesByIds(distinctiveIds);

        Product product = new Product(id, name, quantity, price, description, status, image1Path, image2Path,isHot, category, productDistinctives);
        Product addedProduct = productService.addProduct(product);

        if (productDistinctives != null && !productDistinctives.isEmpty()) {
            List<ProductDistinctive> productDistinctiveList = productDistinctives.stream()
                .map(distinctive -> new ProductDistinctive(addedProduct, distinctive))
                .collect(Collectors.toList());
            productDistinctiveRepository.saveAll(productDistinctiveList);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProductById(@PathVariable String id,
                                                     @RequestParam String name,
                                                     @RequestParam int quantity,
                                                     @RequestParam double price,
                                                     @RequestParam String description,
                                                     @RequestParam String status,
                                                     @RequestParam boolean isHot,
                                                     @RequestParam String categoryId, 
                                                     @RequestParam List<String> distinctiveIds,
                                                     @RequestParam(value = "image1", required = false) MultipartFile image1,
                                                     @RequestParam(value = "image2", required = false) MultipartFile image2) {

        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String image1Path = image1 != null ? saveFile(image1) : null;
        String image2Path = image2 != null ? saveFile(image2) : null;

        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<Distinctive> productDistinctives = distinctiveService.getDistinctivesByIds(distinctiveIds);
        if (productDistinctives == null || productDistinctives.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (!existingProductOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Product existingProduct = existingProductOptional.get();
        productDistinctiveRepository.deleteByProductId(id);

        if (image1Path != null) existingProduct.setImage1(image1Path);
        if (image2Path != null) existingProduct.setImage2(image2Path);

        existingProduct.setName(name);
        existingProduct.setQuantity(quantity);
        existingProduct.setPrice(price);
        existingProduct.setDescription(description);
        existingProduct.setStatus(status);
        existingProduct.setIsHot(isHot);
        existingProduct.setCategory(category);
        existingProduct.setDistinctives(productDistinctives);

        Product savedProduct = productService.updateProductById(id, existingProduct);
        List<ProductDistinctive> newProductDistinctives = productDistinctives.stream()
                .map(distinctive -> new ProductDistinctive(savedProduct, distinctive))
                .collect(Collectors.toList());
        productDistinctiveRepository.saveAll(newProductDistinctives);

        return ResponseEntity.ok(savedProduct);
    }


private String saveFile(MultipartFile file) {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    File dir = new File(IMAGE_DIR);
    if (!dir.exists()) dir.mkdirs();

    String filePath = Paths.get(IMAGE_DIR, fileName).toString();
    try (FileOutputStream fos = new FileOutputStream(filePath)) {
        fos.write(file.getBytes());
        return filePath; // Trả về đường dẫn file
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}
@GetMapping("/hot")
public ResponseEntity<List<Product>> getHotProducts() {
    List<Product> hotProducts = productService.getHotProducts();
    if (hotProducts.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(hotProducts);
    }
    return ResponseEntity.ok(hotProducts);
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

            // Lấy sản phẩm và danh sách các liên kết trong bảng ProductDistinctive
            Product product = productOptional.get();

            // Xóa các liên kết trong bảng ProductDistinctive (nếu có)
            List<ProductDistinctive> productDistinctives = productDistinctiveRepository.findByProductId(id);
            if (!productDistinctives.isEmpty()) {
                // Trước khi xóa, bạn cần xử lý xóa hoặc làm sạch các bản ghi trong bảng ProductDistinctive
                for (ProductDistinctive pd : productDistinctives) {
                    // Xóa liên kết trong bảng ProductDistinctive
                    productDistinctiveRepository.delete(pd);
                }
            }

            // Sau khi xóa các bản ghi trong ProductDistinctive, bạn có thể xóa sản phẩm
            productRepository.deleteById(id); // Xóa sản phẩm

            return ResponseEntity.ok("Product and related records deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting product: " + e.getMessage());
        }
    }
    @PutMapping("/{id}/update-quantity")
    public ResponseEntity<?> updateProductQuantity(@PathVariable String id, @RequestParam int quantity) {
        try {
            productService.updateQuantity(id, quantity);
            return ResponseEntity.ok("Product quantity updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
