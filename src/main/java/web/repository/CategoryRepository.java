package web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import web.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
	 List<Category> findByIsHotTrue();
	 @Query("SELECT c FROM Category c JOIN FETCH c.products p WHERE c.isHot = true AND p.isHot = true")
	    List<Category> findHotCategoriesWithProducts();
}

