package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.ProductDistinctive;
import java.util.List;

@Repository
public interface ProductDistinctiveRepository extends JpaRepository<ProductDistinctive, Long> {

    List<ProductDistinctive> findByProductId(String productId);
    
    List<ProductDistinctive> findByDistinctiveId(String distinctiveId);
}
