package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import web.model.Category;
import web.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved categories"),
        @ApiResponse(responseCode = "204", description = "No categories found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/get/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(categories); // Return 204 if no categories found
        }
        return ResponseEntity.ok(categories); // Return 200 with list of categories
    }

    @Operation(summary = "Get category by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved category"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("get/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable String id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category); // Return 200 if category found
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new CategoryErrorResponse(e.getReason()));
        }
    }

    @Operation(summary = "Add a new category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid category data"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        try {
            Category addedCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedCategory); // Return 201 if addition is successful
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new CategoryErrorResponse(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the category: " + e.getMessage());
        }
    }

    @Operation(summary = "Update a category by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "400", description = "Invalid data provided"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoryById(@PathVariable String id, @RequestBody Category updatedCategory) {
        try {
            // Update category including the isHot field
            Category category = categoryService.updateCategory(id, updatedCategory);
            return ResponseEntity.ok(category);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new CategoryErrorResponse(e.getReason()));
        }
    }

    @Operation(summary = "Delete a category by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted category"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new CategoryErrorResponse(e.getReason()));
        }
    }

    // Method to get categories where isHot = true
    @Operation(summary = "Get all hot categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved hot categories"),
        @ApiResponse(responseCode = "204", description = "No hot categories found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/hot")
    public ResponseEntity<List<Category>> getHotCategories() {
        List<Category> hotCategories = categoryService.findByIsHotTrue();
        if (hotCategories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(hotCategories); // Return 204 if no hot categories found
        }
        return ResponseEntity.ok(hotCategories); // Return 200 with list of hot categories
    }
    
    // Inner class for error responses
    public static class CategoryErrorResponse {
        private String message;

        public CategoryErrorResponse(String message) {
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
