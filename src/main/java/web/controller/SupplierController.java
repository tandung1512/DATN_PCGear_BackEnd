package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @Operation(summary = "Get supplier by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved supplier"),
        @ApiResponse(responseCode = "404", description = "Supplier not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{id}")
    public Supplier getSupplierById(@PathVariable String id) {
        return supplierService.getSupplierById(id);
    }

    @Operation(summary = "Save a supplier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Supplier successfully created"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        return supplierService.saveSupplier(supplier);
    }

    @Operation(summary = "Delete a supplier by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted supplier"),
        @ApiResponse(responseCode = "404", description = "Supplier not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable String id) {
        supplierService.deleteSupplier(id);
    }
}
