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
@Table(name = "suppliers")
public class Supplier {

    @Id
   
    private String id;  // id là kiểu String vì trong DB là VARCHAR(20)

    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    @OneToMany(mappedBy = "supplier")
    private List<StockReceipt> stockReceipts;
}
