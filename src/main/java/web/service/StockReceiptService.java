package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import web.model.StockReceipt;
import web.repository.StockReceiptRepository;

import java.util.List;

@Service
public class StockReceiptService {

    @Autowired
    private StockReceiptRepository stockReceiptRepository;

    // Lấy tất cả các StockReceipt
    public List<StockReceipt> getAllStockReceipts() {
        return stockReceiptRepository.findAll();
    }

    // Lấy StockReceipt theo ID
    public StockReceipt getStockReceiptById(Long id) {
        return stockReceiptRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "StockReceipt with ID " + id + " not found"));
    }

    // Thêm StockReceipt mới
    public StockReceipt addStockReceipt(StockReceipt stockReceipt) {
        // Kiểm tra nếu các trường cần thiết có hợp lệ
        if (stockReceipt.getProduct() == null || stockReceipt.getSupplier() == null || stockReceipt.getBrand() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product, Supplier, and Brand are required.");
        }

        // Lưu StockReceipt vào cơ sở dữ liệu
        return stockReceiptRepository.save(stockReceipt);
    }

    // Cập nhật StockReceipt theo ID
    public StockReceipt updateStockReceipt(Long id, StockReceipt updatedStockReceipt) {
        StockReceipt existingStockReceipt = stockReceiptRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "StockReceipt with ID " + id + " not found"));

        // Cập nhật thông tin của StockReceipt
        existingStockReceipt.setQuantity(updatedStockReceipt.getQuantity());
        existingStockReceipt.setPrice(updatedStockReceipt.getPrice());
        existingStockReceipt.setOrderDate(updatedStockReceipt.getOrderDate());
        existingStockReceipt.setProduct(updatedStockReceipt.getProduct());
        existingStockReceipt.setSupplier(updatedStockReceipt.getSupplier());
        existingStockReceipt.setBrand(updatedStockReceipt.getBrand());

        // Lưu lại vào cơ sở dữ liệu
        return stockReceiptRepository.save(existingStockReceipt);
    }

    // Xóa StockReceipt theo ID
    public void deleteStockReceipt(Long id) {
        if (!stockReceiptRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "StockReceipt with ID " + id + " not found");
        }
        stockReceiptRepository.deleteById(id);
    }
}
