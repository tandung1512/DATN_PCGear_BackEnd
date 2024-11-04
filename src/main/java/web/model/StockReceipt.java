package web.model;
 

import java.util.Date;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stock_receipts")
public class StockReceipt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "supplier_id")
	private Supplier supplier;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;

	private int quantity;

	private float price;

	@Temporal(TemporalType.DATE)
	private Date orderDate;

	
}