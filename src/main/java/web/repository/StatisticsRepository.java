package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.model.*;

@Repository
public interface StatisticsRepository extends JpaRepository<Product, String> {
    
    @Query("SELECT COUNT(p) FROM Product p")
    Long countProducts();

    @Query("SELECT COUNT(a) FROM Account a")
    Long countUsers();

    @Query("SELECT COUNT(c) FROM Category c")
    Long countCategories();
}
