package web.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "invoices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    private String id;

    private String address;
    private String status;
    private String node;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @PrePersist
    public void setOrderDate() {
        if (this.orderDate == null) {
            this.orderDate = new Date();
        }
        if (this.id == null) {
            this.id = UUID.randomUUID().toString().substring(0, 16);
        }
    }

    @Column(name = "total_amount")
    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account user;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<DetailedInvoice> detailedInvoices;

    public String StatusName() {
        String statusName = "";
        switch (status) {
            case "pending":
                statusName = "Đang xác nhận";
                break;
            case "cancelled":
                statusName = "Đã hủy";
                break;
            case "delivery":
                statusName = "Đang vận chuyển";
                break;
            case "complete":
                statusName = "Đã giao thành công";
                break;
        }
        return statusName;
    }
}
