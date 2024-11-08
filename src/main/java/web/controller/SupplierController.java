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

    @Operation(summary = "Get all suppliers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved suppliers"),
        @ApiResponse(responseCode = "204", description = "No content found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        if (suppliers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(suppliers);
        }
        return ResponseEntity.ok(suppliers);
    }

    @Operation(summary = "Get supplier by id")
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

    @Operation(summary = "Add a new supplier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Supplier added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid supplier data"),
        @ApiResponse(responseCode = "409", description = "Supplier ID already exists"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/add")
    public ResponseEntity<?> addSupplier(@RequestBody Supplier supplier) {
        try {
            // Kiểm tra tính hợp lệ của các trường
            if (supplier.getId() == null || supplier.getId().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SupplierErrorResponse("Supplier ID is required."));
            }

            if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SupplierErrorResponse("Supplier name is required."));
            }

          

            // Lưu Supplier vào cơ sở dữ liệu
            Supplier addedSupplier = supplierService.addSupplier(supplier);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedSupplier);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new SupplierErrorResponse(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SupplierErrorResponse("An error occurred while adding the supplier: " + e.getMessage()));
        }
    }

    @Operation(summary = "Delete a supplier by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted supplier"),
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SupplierErrorResponse("An error occurred while deleting the supplier: " + e.getMessage()));
        }
    }

    // Error response class
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
