package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import web.model.Account;
import web.repository.AccountRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private PasswordEncoder passwordEncoder; // For encoding passwords

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(String id) {
        return accountRepository.findById(id);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public void delete(String id) {
        accountRepository.deleteById(id);
    }

    // Register a new account as a regular user (admin set to false by default)
    public void registerAccount(String id, String name, String password, String phone, String email, String address, MultipartFile imageFile) throws Exception {

        // Check if ID (username) already exists
        Optional<Account> existingAccount = accountRepository.findById(id);
        if (existingAccount.isPresent()) {
            throw new Exception("Username (ID) has already been used.");
        }
        
        // Encode password before saving to database
        String encodedPassword = passwordEncoder.encode(password);

        // Handle image saving if provided
        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imagePath = saveImage(imageFile); // Save image
        }

        // Create a new account with default properties
        Account newAccount = Account.builder()
                .id(id)
                .name(name)
                .password(encodedPassword)
                .phone(phone)
                .email(email)
                .address(address)
                .image(imagePath)
                .admin(false) // Set to false to create a regular user account
                .status(true) // Default to true
                .confirm(true) // Default to true
                .otp(null) // Default to null
                .build();

        accountRepository.save(newAccount); // Save new account
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        // Get the image save path from servlet context
        String uploadsDir = servletContext.getRealPath("/webapp/files/images/");

        // Create directory if it doesn't exist
        Path uploadPath = Paths.get(uploadsDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Create file name using the original file name
        String fileName = imageFile.getOriginalFilename(); // Get the original file name
        Path imagePath = uploadPath.resolve(fileName); // Create the file path

        // Copy file from input stream
        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        return imagePath.toString(); // Return the file path
    }
}
