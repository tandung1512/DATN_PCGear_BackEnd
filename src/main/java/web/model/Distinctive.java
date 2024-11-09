package web.model;

 

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "distinctives")
public class Distinctive {
    @Id
    
    private String id;

    private String name;

//    @OneToMany(mappedBy = "distinctive")
//    private List<ProductDistinctive> productDistinctives;
}

