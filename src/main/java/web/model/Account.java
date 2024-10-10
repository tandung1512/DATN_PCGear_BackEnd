package web.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Accounts") // Define the table name in the database
public class Account {
    @Id
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Email(message = "Email should be valid")
    private String email;

    private String address;
    private String image;
    private boolean admin;
    private boolean status;
    private Boolean confirm; // Changed to Boolean to allow null
    private String otp;

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
