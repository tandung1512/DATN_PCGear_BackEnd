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
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String orderDate;
    private String address;
    private String status;
    private String node;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account user;

    @OneToMany(mappedBy = "invoice")
    private List<DetailedInvoice> detailedInvoices;
}
