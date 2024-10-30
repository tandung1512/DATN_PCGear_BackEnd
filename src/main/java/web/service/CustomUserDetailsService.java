package web.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.model.Account;
import web.repository.AccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // This method is required by UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Here, we assume that username can be an ID
        return loadUserById(username); // Directly call your custom load method
    }

    // Load user by ID
    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        // Find account by ID
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));
        
        // Return the CustomUserDetails object
        return new CustomUserDetails(account);
    }
}
