package web.repository;

import java.util.Date;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.model.DetailedInvoice;
import web.model.Invoice;
import web.model.Product;
import web.model.ReportRevenue_Quantity;
import web.model.ReportTotalRevenueDetail;

@Repository
public interface DetailedInvoiceRepository extends JpaRepository<DetailedInvoice, Long> {
       // Các phương thức truy vấn tùy chỉnh nếu cần
       List<DetailedInvoice> findByInvoiceId(String invoiceId);
       Optional<DetailedInvoice> findByInvoiceAndProduct(Invoice invoice, Product product);
      
     
        



}