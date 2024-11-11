package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import web.model.ProductDistinctive;
import java.util.List;

@Repository
public interface ProductDistinctiveRepository extends JpaRepository<ProductDistinctive, Long> {

    List<ProductDistinctive> findByProductId(String productId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductDistinctive pd WHERE pd.product.id = :productId")
    void deleteByProductId(String productId);
    
    List<ProductDistinctive> findByDistinctiveId(String distinctiveId);
}
