package web.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detailed_invoices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    @Column(name = "payment_method")
    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    @JsonBackReference
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // Giữ nguyên nếu `Product` không gây vòng lặp
}
