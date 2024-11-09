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
@Table(name = "categories")
public class Category {
	@Id

	private String id; // id là kiểu String vì trong DB là VARCHAR(20)

	private String name;
	private String description;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "category")
	
	private List<Product> products;
}
