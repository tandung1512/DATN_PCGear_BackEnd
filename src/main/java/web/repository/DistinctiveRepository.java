package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.Distinctive;

@Repository
public interface DistinctiveRepository extends JpaRepository<Distinctive, String> {
    // Các phương thức CRUD cơ bản sẽ được tự động cung cấp bởi JpaRepository
}
