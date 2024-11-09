package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import web.model.Brand;
import web.service.BrandService;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Operation(summary = "Get all brands")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved brands"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        if (brands.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(brands); // Return 204 if no brands found
        }
        return ResponseEntity.ok(brands); // Return 200 with list of brands
    }

    @Operation(summary = "Get brand by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved brand"),
        @ApiResponse(responseCode = "404", description = "Brand not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable String id) {
        try {
            Brand brand = brandService.getBrandById(id); // Now directly gets the Brand object
            return ResponseEntity.ok(brand); // Return 200 if brand found
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new BrandErrorResponse(e.getReason())); // Return error response
        }
    }

    @Operation(summary = "Add a new brand")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Brand added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid brand data"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/add")
    public ResponseEntity<?> addBrand(@RequestBody Brand brand) {
        try {
            Brand addedBrand = brandService.addBrand(brand); // Sử dụng phương thức addBrand từ BrandService
            return ResponseEntity.status(HttpStatus.CREATED).body(addedBrand); // Return 201 if addition is successful
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new BrandErrorResponse(e.getReason())); // Handle error
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the brand: " + e.getMessage());
        }
    }
    
    @Operation(summary = "Update a brand by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Brand updated successfully"),
        @ApiResponse(responseCode = "404", description = "Brand not found"),
        @ApiResponse(responseCode = "400", description = "Invalid data provided"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrandById(@PathVariable String id, @RequestBody Brand updatedBrand) {
        try {
            Brand brand = brandService.updateBrandById(id, updatedBrand);
            return ResponseEntity.ok(brand); 
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new BrandErrorResponse(e.getReason()));
        }
    }


    @Operation(summary = "Delete a brand by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted brand"),
        @ApiResponse(responseCode = "404", description = "Brand not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable String id) {
        try {
            brandService.deleteBrand(id); // Attempt to delete the brand
            return ResponseEntity.ok("Brand deleted successfully"); // Return 200 if deletion successful
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new BrandErrorResponse(e.getReason())); // Handle error
        }
    }

    // Error response class
    public static class BrandErrorResponse {
        private String message;

        public BrandErrorResponse(String message) {
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
