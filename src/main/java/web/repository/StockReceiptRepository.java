package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.StockReceipt;

@Repository
public interface StockReceiptRepository extends JpaRepository<StockReceipt, Long> {
    // Tự động cung cấp các phương thức CRUD cơ bản
}
