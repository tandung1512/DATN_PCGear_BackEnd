package web.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRevenue_Quantity {
    
    private double totalRevenue;
	private long totalQuantity;
	
}
