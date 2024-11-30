package web.model;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Addresses {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    
	    @Column(name = "detail")
	    private String detail;

	    @Column(name = "ward")
	    private String ward;

	    @Column(name = "district")
	    private String district;

	    @Column(name = "province")
	    private String province;

	    @Column(name = "is_default")
	    private Boolean isDefault;
	    @ManyToOne
	    @JoinColumn(name = "account_id", nullable = false)
	    @JsonIgnore
	    private Account account;  
}
