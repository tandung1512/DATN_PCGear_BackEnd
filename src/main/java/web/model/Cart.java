package web.model;

 

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quantity;
    private String orderDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account user;

//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;
//    
}
