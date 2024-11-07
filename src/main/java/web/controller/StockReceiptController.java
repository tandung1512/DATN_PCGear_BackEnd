package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public List<StockReceipt> getAllStockReceipts() {
        return stockReceiptService.getAllStockReceipts();
    }

    @Operation(summary = "Get stock receipt by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved stock receipt"),
        @ApiResponse(responseCode = "404", description = "Stock receipt not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{id}")
    public StockReceipt getStockReceiptById(@PathVariable Long id) {
        return stockReceiptService.getStockReceiptById(id);
    }

    @Operation(summary = "Save a stock receipt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Stock receipt successfully created"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockReceipt createStockReceipt(@RequestBody StockReceipt stockReceipt) {
        return stockReceiptService.saveStockReceipt(stockReceipt);
    }

    @Operation(summary = "Delete a stock receipt by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted stock receipt"),
        @ApiResponse(responseCode = "404", description = "Stock receipt not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/{id}")
    public void deleteStockReceipt(@PathVariable Long id) {
        stockReceiptService.deleteStockReceipt(id);
    }
}
