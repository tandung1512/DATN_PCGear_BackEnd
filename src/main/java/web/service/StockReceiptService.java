package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.StockReceipt;
import web.repository.StockReceiptRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StockReceiptService {

    @Autowired
    private StockReceiptRepository stockReceiptRepository;

    public List<StockReceipt> getAllStockReceipts() {
        return stockReceiptRepository.findAll();
    }

    public StockReceipt getStockReceiptById(Long id) {
        Optional<StockReceipt> stockReceipt = stockReceiptRepository.findById(id);
        return stockReceipt.orElse(null);  // Trả về null nếu không tìm thấy
    }

    public StockReceipt saveStockReceipt(StockReceipt stockReceipt) {
        return stockReceiptRepository.save(stockReceipt);
    }

    public void deleteStockReceipt(Long id) {
        stockReceiptRepository.deleteById(id);
    }
}
