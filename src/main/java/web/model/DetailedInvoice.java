package web.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Long id;

    private int quantity; 
    private String paymentMethod; 

    @ManyToOne
    
    @JoinColumn(name = "invoice_id", nullable = false)
    @JsonBackReference 
    private Invoice invoice; 
    @ManyToOne
    
    @JoinColumn(name = "product_id")
    private Product product;
}
