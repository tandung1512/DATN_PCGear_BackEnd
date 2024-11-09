package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import web.model.StockReceipt;
import web.service.StockReceiptService;

import java.util.List;

@RestController
@RequestMapping("/api/stock-receipts")
public class StockReceiptController {

    @Autowired
    private StockReceiptService stockReceiptService;

    @Operation(summary = "Get all stock receipts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved stock receipts"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    public ResponseEntity<List<StockReceipt>> getAllStockReceipts() {
        List<StockReceipt> stockReceipts = stockReceiptService.getAllStockReceipts();
        if (stockReceipts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(stockReceipts); // Return 204 if no stock receipts found
        }
        return ResponseEntity.ok(stockReceipts); // Return 200 with list of stock receipts
    }

    @Operation(summary = "Get stock receipt by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved stock receipt"),
        @ApiResponse(responseCode = "404", description = "Stock receipt not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getStockReceiptById(@PathVariable Long id) {
        try {
            StockReceipt stockReceipt = stockReceiptService.getStockReceiptById(id); // Now directly gets the StockReceipt object
            return ResponseEntity.ok(stockReceipt); // Return 200 if stock receipt found
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new StockReceiptErrorResponse(e.getReason())); // Return error response
        }
    }

    @Operation(summary = "Add a new stock receipt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Stock receipt added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid stock receipt data"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/add")
    public ResponseEntity<?> addStockReceipt(@RequestBody StockReceipt stockReceipt) {
        try {
            StockReceipt addedStockReceipt = stockReceiptService.addStockReceipt(stockReceipt); // Add stock receipt
            return ResponseEntity.status(HttpStatus.CREATED).body(addedStockReceipt); // Return 201 if addition is successful
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new StockReceiptErrorResponse(e.getReason())); // Handle error
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the stock receipt: " + e.getMessage());
        }
    }

    @Operation(summary = "Update a stock receipt by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock receipt updated successfully"),
        @ApiResponse(responseCode = "404", description = "Stock receipt not found"),
        @ApiResponse(responseCode = "400", description = "Invalid stock receipt data"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStockReceipt(@PathVariable Long id, @RequestBody StockReceipt updatedStockReceipt) {
        try {
            StockReceipt stockReceipt = stockReceiptService.updateStockReceipt(id, updatedStockReceipt); // Update stock receipt
            return ResponseEntity.ok(stockReceipt); // Return 200 if update is successful
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new StockReceiptErrorResponse(e.getReason())); // Handle error
        }
    }

    @Operation(summary = "Delete a stock receipt by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted stock receipt"),
        @ApiResponse(responseCode = "404", description = "Stock receipt not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStockReceipt(@PathVariable Long id) {
        try {
            stockReceiptService.deleteStockReceipt(id); // Delete stock receipt
            return ResponseEntity.ok("Stock receipt deleted successfully"); // Return 200 if deletion successful
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new StockReceiptErrorResponse(e.getReason())); // Handle error
        }
    }

    // Error response class
    public static class StockReceiptErrorResponse {
        private String message;

        public StockReceiptErrorResponse(String message) {
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
