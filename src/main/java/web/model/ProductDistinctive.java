package web.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products_distinctives")
public class ProductDistinctive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Use unique @JsonBackReference name for Product relationship
    @JsonBackReference("product-productDistinctives")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    // Sử dụng @JsonBackReference cho quan hệ với Distinctive để tránh vòng lặp
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "distinctive_id", nullable = true)
    private Distinctive distinctive;
}
