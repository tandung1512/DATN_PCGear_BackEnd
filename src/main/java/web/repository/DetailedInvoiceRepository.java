package web.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.model.DetailedInvoice;
import web.model.ReportRevenue_Quantity;
import web.model.ReportTotalRevenueDetail;

@Repository
public interface DetailedInvoiceRepository extends JpaRepository<DetailedInvoice, String> {
       // Các phương thức truy vấn tùy chỉnh nếu cần
       List<DetailedInvoice> findByInvoiceId(String invoiceId);

        // tổng doanh thu
       @Query("SELECT new web.model.ReportRevenue_Quantity(SUM(d.quantity * d.product.price)   , SUM(d.quantity)  ) "
                     + " FROM DetailedInvoice d " + 
                     "  where d.invoice.status = 'complete'  ")
       List<ReportRevenue_Quantity> getTotalRevenueAll();


@Query("SELECT new web.model.ReportTotalRevenueDetail(" +
        " Count(d.id)   , SUM(d.quantity) , i.orderDate , sum(p.price * d.quantity) ) " +
       " from Invoice i  join i.detailedInvoices d join d.product p" +
       " where i.status = 'complete' " +
       " group by i.orderDate ")
List<ReportTotalRevenueDetail> getReportTotalRevenueDetails();

@Query("SELECT new web.model.ReportTotalRevenueDetail(" +
        " Count(d.id)   , SUM(d.quantity) , i.orderDate , sum(p.price * d.quantity) ) " +
       " from Invoice i  join i.detailedInvoices d join d.product p" +
       " where i.status = 'complete'  AND  i.orderDate BETWEEN ?1 AND ?2 " +
       " group by i.orderDate ")
List<ReportTotalRevenueDetail> findTotalRevenueDetails(Date startDate , Date endDate);

     

}