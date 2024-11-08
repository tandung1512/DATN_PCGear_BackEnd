package web.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTotalRevenueDetail {

    private Long usetotalPurchaseAmountrName;
    private Long totalNumberWarehouses;
    private Date orderDate;
    private Double TotalRevenue;

   

}
