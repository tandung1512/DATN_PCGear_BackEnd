package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {
    // Tự động cung cấp các phương thức CRUD cơ bản
  
}
