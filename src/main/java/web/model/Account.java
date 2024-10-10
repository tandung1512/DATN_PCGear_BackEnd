package web.model;


import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Accounts")
public class Account {
    @Id
    private String id; // Remains String to match varchar(20)

    private String name;
    private String password; // Consider using a secure way to handle passwords
    private String phone;
    private String email;
    private String address;
    private String image;
    private boolean admin; // Should be `true` or `false`
    private boolean status; // Should be `true` or `false`
    private Boolean confirm; // Use Boolean object to allow null
    private String otp; // New field for OTP

    @OneToMany(mappedBy = "user") // Assuming 'user' is the field in Comment
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "user") // Assuming 'user' is the field in Invoice
    @JsonIgnore
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "user") // Assuming 'user' is the field in Cart
    @JsonIgnore
    private List<Cart> carts;

    @OneToMany(mappedBy = "user") // Assuming 'user' is the field in UserHistory
    @JsonIgnore
    private List<UserHistory> userHistories;

    @Override
    public String toString() {
        return "Account{" + 
               "id='" + id + '\'' + 
               ", name='" + name + '\'' + 
               ", password='" + password + '\'' + 
               ", phone='" + phone + '\'' + 
               ", email='" + email + '\'' + 
               ", address='" + address + '\'' + 
               ", image='" + image + '\'' + 
               ", admin=" + admin + 
               ", status=" + status + 
               ", confirm=" + confirm + 
               ", otp='" + otp + '\'' + 
               '}';
    }
}
