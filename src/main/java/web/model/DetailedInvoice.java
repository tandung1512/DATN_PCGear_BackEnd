package web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "detailed_invoices")
public class DetailedInvoice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Khóa chính, kiểu Long

    private int quantity; // Số lượng sản phẩm
    private String paymentMethod; // Phương thức thanh toán

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice; // Liên kết với bảng hóa đơn
    @ManyToOne
    
    @JoinColumn(name = "product_id")
    private Product product;
}
