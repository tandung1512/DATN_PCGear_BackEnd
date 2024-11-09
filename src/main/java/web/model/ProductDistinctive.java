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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id")
    
    private Product product;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "distinctive_id")
    
    private Distinctive distinctive;
}
