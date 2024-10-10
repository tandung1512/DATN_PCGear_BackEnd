package web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  

    private String content;
    private int likeCount;
    private String orderDate;

    @ManyToOne
    private Account user;  // Ensure this matches with your Account class association

    
    @ManyToOne
    private Product product;
    // Add other fields as necessary
}