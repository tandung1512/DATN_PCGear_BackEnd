package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
    // Tự động cung cấp các phương thức CRUD cơ bản
}