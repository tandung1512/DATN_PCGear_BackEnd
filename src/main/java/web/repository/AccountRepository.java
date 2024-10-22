package web.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    // Custom query methods can be defined here if needed

    // Example: Find by email
    Account findByEmail(String email);

    // Example: Check if a user exists by ID
    boolean existsById(String id);
    
    Optional<Account> findById(String id);
}
