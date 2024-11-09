package web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;




@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
	@Id
	
	private String id; // id là kiểu String vì trong DB là VARCHAR(20)

	private String name;
	private int quantity;
	private double price;
	private String description;
	private String status;
	private String image1;
	private String image2;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

//	@OneToMany(mappedBy = "product")
//	private List<Comment> comments;
//
//	@OneToMany(mappedBy = "product")
//	private List<Cart> carts;
//
//	@OneToMany(mappedBy = "product")
//	private List<DetailedInvoice> detailedInvoices;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "product")
	
	private List<ProductDistinctive> productDistinctives;
	
	
}
