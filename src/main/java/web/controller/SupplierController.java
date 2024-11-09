package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import web.model.Supplier;
import web.service.SupplierService;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // Get all suppliers
    @Operation(summary = "Get all suppliers", description = "Lấy danh sách tất cả các nhà cung cấp")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved suppliers"),
        @ApiResponse(responseCode = "204", description = "No suppliers found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return suppliers.isEmpty() 
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(suppliers)
                : ResponseEntity.ok(suppliers);
    }

    // Get supplier by ID
    @Operation(summary = "Get supplier by ID", description = "Lấy thông tin của nhà cung cấp dựa trên ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved supplier"),
        @ApiResponse(responseCode = "404", description = "Supplier not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable String id) {
        try {
            Supplier supplier = supplierService.getSupplierById(id);
            return ResponseEntity.ok(supplier);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new SupplierErrorResponse(e.getReason()));
        }
    }

    // Add new supplier
    @Operation(summary = "Add a new supplier", description = "Thêm mới nhà cung cấp vào hệ thống")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Supplier added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid supplier data"),
        @ApiResponse(responseCode = "409", description = "Supplier ID already exists"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/add")
    public ResponseEntity<?> addSupplier(@RequestBody Supplier supplier) {
        try {
            Supplier addedSupplier = supplierService.addSupplier(supplier);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedSupplier);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new SupplierErrorResponse(e.getReason()));
        }
    }

    // Update supplier by ID
    @Operation(summary = "Update supplier by ID", description = "Cập nhật thông tin của nhà cung cấp dựa trên ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Supplier updated successfully"),
        @ApiResponse(responseCode = "404", description = "Supplier not found"),
        @ApiResponse(responseCode = "400", description = "Invalid supplier data"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplierById(@PathVariable String id, @RequestBody Supplier supplier) {
        try {
            Supplier updatedSupplier = supplierService.updateSupplierById(id, supplier);
            return ResponseEntity.ok(updatedSupplier);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new SupplierErrorResponse(e.getReason()));
        }
    }

    // Delete supplier by ID
    @Operation(summary = "Delete supplier by ID", description = "Xóa nhà cung cấp khỏi hệ thống dựa trên ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Supplier deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Supplier not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable String id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.ok("Supplier deleted successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new SupplierErrorResponse(e.getReason()));
        }
    }

    // Error response class for handling errors
    public static class SupplierErrorResponse {
        private String message;

        public SupplierErrorResponse(String message) {
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
