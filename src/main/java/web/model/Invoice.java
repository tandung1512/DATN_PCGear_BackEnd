package web.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Date orderDate;
    private String address;
    private String status;
    private String node;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account user;

    @OneToMany(mappedBy = "invoice")
    private List<DetailedInvoice> detailedInvoices;
    public String getStatusName() {
		String statusName = "";
		switch (status) {
		case "pending":
			statusName = "Đang xác nhận";
			break;
		case "cancelled":
			statusName = "Đã hủy";
			break;
		case "delivery":
			statusName = "Đang vận chuyển";
			break;
		case "complete":
			statusName = "Đã giao thành công";
			break;
		}
		return statusName;
	}


}
