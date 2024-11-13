package web.model;

 

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "distinctives")
public class Distinctive {
    @Id
    
    private String id;

    private String name;

    @JsonBackReference("product-distinctives")
    @JsonIgnore
    @ManyToMany(mappedBy = "distinctives")
    private List<Product> products;


//    @OneToMany(mappedBy = "distinctive")
//    private List<ProductDistinctive> productDistinctives;
}

