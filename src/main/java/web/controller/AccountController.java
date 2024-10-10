package web.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import web.model.Account;
import web.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired 
    private AccountService accountService;

    // Get all accounts
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        return ResponseEntity.ok(accounts);
    }

    // Get account by ID
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable String id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new account
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        Account createdAccount = accountService.save(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    // Update account by ID
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable String id, @Valid @RequestBody Account account) {
        return accountService.findById(id)
                .map(existingAccount -> {
                    account.setId(id); // Ensure the ID in the account object is correct
                    Account updatedAccount = accountService.save(account);
                    return ResponseEntity.ok(updatedAccount);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete account by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        if (accountService.findById(id).isPresent()) {
            accountService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
