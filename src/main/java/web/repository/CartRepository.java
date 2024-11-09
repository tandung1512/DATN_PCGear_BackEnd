package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    // Custom queries (if needed) can be added here in the future.
    // JpaRepository provides built-in methods like save(), findAll(), findById(), deleteById(), etc.
}
