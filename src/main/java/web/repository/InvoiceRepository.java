package web.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import web.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    @Query("SELECT i FROM Invoice i WHERE i.user.id = :username AND i.status = :status "
            + "GROUP BY i.id, i.orderDate, i.address, i.status, i.user.id, i.node "
            + "ORDER BY i.orderDate DESC")
    List<Invoice> findByUsernameAndStatus(@Param("username") String username, @Param("status") String status);
    List<Invoice> findByStatusContainingIgnoreCase(String keyword);
    
    @Query(value = """
    	    SELECT 
    	        CONVERT(DATE, i.order_date) AS ngay, 
    	        SUM(p.price * di.quantity) AS tongtiendaban
    	    FROM Invoices i
    	    JOIN detailed_invoices di ON i.id = di.invoice_id
    	    JOIN Products p ON di.product_id = p.id
    	    WHERE MONTH(i.order_date) = :month AND YEAR(i.order_date) = YEAR(GETDATE())
    	    GROUP BY CONVERT(DATE, i.order_date)
    	    """, nativeQuery = true)
    	List<Map<String, Object>> getMonthlySales(@Param("month") int month);
    
    @Query(value = """
    	    SELECT 
    	        MONTH(i.order_date) AS thang, 
    	        SUM(p.price * di.quantity) AS tongtiendaban
    	    FROM Invoices i
    	    JOIN detailed_invoices di ON i.id = di.invoice_id
    	    JOIN Products p ON di.product_id = p.id
    	    WHERE YEAR(i.order_date) = YEAR(GETDATE())
    	    GROUP BY MONTH(i.order_date)
    	    ORDER BY thang
    	    """, nativeQuery = true)
    	List<Map<String, Object>> getYearlySales();

}