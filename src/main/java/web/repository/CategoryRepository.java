package web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
	 List<Category> findByIsHotTrue();
}
