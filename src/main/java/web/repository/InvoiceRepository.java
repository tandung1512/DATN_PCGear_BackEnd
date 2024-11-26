package web.repository;

import java.util.List;

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
}
