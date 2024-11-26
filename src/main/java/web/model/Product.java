package web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

	@Id
	private String id; // ID là kiểu String vì trong DB là VARCHAR(20)

	private String name;
	private int quantity;
	private double price;
	private String description;
	private String status;
	private String image1;
	private String image2;
//	@Column(name = "isHot", columnDefinition = "BIT(1) DEFAULT 0", nullable = false)
	@Column(name = "is_hot")
	private boolean isHot;


	// Constructor đầy đủ với các trường image1 và image2 thay vì image1Base64,
	
	public Product(String id, String name, int quantity, double price, String description, String status, String image1,
			String image2, boolean isHot, Category category, List<Distinctive> distinctives) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.description = description;
		this.status = status;
		this.image1 = image1;
		this.image2 = image2;
		this.isHot = isHot;
		this.category = category;
		this.distinctives = distinctives;
	}
	 public boolean getIsHot() {
	        return isHot;
	    }

	    public void setIsHot(boolean isHot) {
	        this.isHot = isHot;
	    }
	
//	   public boolean isHot() {
//	        return isHot;
//	    }
//
//	    public void setHot(boolean isHot) {
//	        this.isHot = isHot;
//	    }
	@JsonBackReference("product-category") // Unique name for Category reference
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = true)
	private Category category;

	// Quan hệ ManyToMany với Distinctive
	 @JsonIgnore
	@JsonManagedReference("product-distinctives")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "product_distinctives", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "distinctive_id"))
	private List<Distinctive> distinctives;

	// Constructor with images and category

	// Các quan hệ đã bị comment nhưng vẫn giữ lại trong trường hợp cần sử dụng sau
	// này

	// @OneToMany(mappedBy = "product")
	// private List<Comment> comments;

	// @OneToMany(mappedBy = "product")
	// private List<Cart> carts;

	@OneToMany(mappedBy = "product")
	@JsonIgnore
	private List<DetailedInvoice> detailedInvoices;
	
}
