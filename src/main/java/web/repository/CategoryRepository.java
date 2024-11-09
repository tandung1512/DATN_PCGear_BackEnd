package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
