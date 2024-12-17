package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import web.model.Account;
import web.model.Addresses;
import web.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
   
    @Operation(summary = "Get all accounts", description = "Retrieve a list of all registered accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Account.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get account by ID", description = "Retrieve account details using account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved account",
                content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Account.class)) }),
        @ApiResponse(responseCode = "404", description = "Account not found"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(
            @Parameter(description = "ID of the account to be retrieved") @PathVariable String id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Register new account", description = "Register a new user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
    })
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Parameter(description = "ID of the new account") @RequestParam String id,
            @Parameter(description = "Name of the new account holder") @RequestParam String name,
            @Parameter(description = "Password of the new account") @RequestParam String password,
            @Parameter(description = "Phone number of the account holder") @RequestParam String phone,
            @Parameter(description = "Email of the new account") @RequestParam String email,
//            @Parameter(description = "Address of the account holder") @RequestParam List<String> addresses,
            @Parameter(description = "Profile image for the account", required = false) @RequestParam(value = "image", required = false) MultipartFile image) {


        Map<String, String> response = new HashMap<>();
//        try {
//            String encodedPassword = passwordEncoder.encode(password);
//            accountService.registerAccount(id, name, encodedPassword, phone, email, address, image);
//            return ResponseEntity.ok("Account registered successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
        
        try {
            String encodedPassword = passwordEncoder.encode(password);
            accountService.registerAccount(id, name, encodedPassword, phone, email, image);
            response.put("message", "Account registered successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
        	response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }
    }

    @Operation(summary = "Create new account", description = "Create a new account with detailed fields")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully",
                content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Account.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PostMapping
    public ResponseEntity<?> createAccount(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
//            @RequestParam(value = "addresses") List<String> addresses, 
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("admin") boolean admin, // Keeping it as boolean
            @RequestParam(value = "status", defaultValue = "true") boolean status,
            @RequestParam(value = "confirm", defaultValue = "true") boolean confirm) {
        
        try {
            String encodedPassword = passwordEncoder.encode(password);
            Account account = new Account();
            account.setId(id);
            account.setName(name);
            account.setPassword(encodedPassword);
            account.setPhone(phone);
            account.setEmail(email);
            
            account.setAdmin(admin); // Using the boolean value directly
            account.setStatus(status);
            account.setConfirm(confirm);
//            List<Addresses> addressList = new ArrayList<>();
//            for (String addressStr : addresses) {
//                Addresses address = new Addresses();
//                address.setDetail(addressStr);  // Giả sử đây là địa chỉ dưới dạng một chuỗi, bạn có thể mở rộng thành nhiều trường nếu cần
//                address.setAccount(account);
//                addressList.add(address);
//            }
//            account.setAddresses(addressList);

            if (image != null && !image.isEmpty()) {
                String fileName = image.getOriginalFilename();
                account.setImage(fileName);
                // TODO: Save the image to a directory on the server
            } else {
                account.setImage(null);
            }

            Account createdAccount = accountService.save(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating account: " + e.getMessage());
        }
    }

    @Operation(summary = "Update account", description = "Update account details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account updated successfully",
                content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Account.class)) }),
        @ApiResponse(responseCode = "404", description = "Account not found"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(
            @Parameter(description = "ID of the account to be updated") @PathVariable String id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
//            @RequestParam(value = "address", required = false) List<String> addresses,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(value = "admin", required = false) Boolean admin, // Keeping it as Boolean
            @RequestParam(value = "status", required = false) Boolean status) {

        try {
            Account existingAccount = accountService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            if (name != null) existingAccount.setName(name);
            if (password != null) existingAccount.setPassword(passwordEncoder.encode(password));
            if (phone != null) existingAccount.setPhone(phone);
            if (email != null) existingAccount.setEmail(email);
//            if (addresses != null && !addresses.isEmpty()) {
//                // Cập nhật danh sách địa chỉ
//                List<Addresses> addressList = new ArrayList<>();
//                for (String addressStr : addresses) {
//                    Addresses address = new Addresses();
//                    address.setDetail(addressStr);
//                    address.setAccount(existingAccount);
//                    addressList.add(address);
//                }
//                existingAccount.setAddresses(addressList);
//            }
            if (admin != null) existingAccount.setAdmin(admin); // Use the Boolean value directly
            if (status != null) existingAccount.setStatus(status);

            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = imageFile.getOriginalFilename();
                existingAccount.setImage(fileName);
                // TODO: Save the image to a directory on the server
            }

            Account updatedAccount = accountService.save(existingAccount);
            return ResponseEntity.ok(updatedAccount);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating account: " + e.getMessage());
        }
    }
    
    @GetMapping("/profile/{id}")
    public ResponseEntity<Account> getProfileById(
            @Parameter(description = "ID of the account to be retrieved") @PathVariable String id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updateProfileById(
            @PathVariable String id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            Principal principal) {
        try {
            // Kiểm tra người dùng đã đăng nhập
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse("error", "Unauthorized access"));
            }

            String loggedInUserId = principal.getName();

            // Kiểm tra quyền cập nhật hồ sơ
            if (!loggedInUserId.equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(createResponse("error", "You are not allowed to update another user's profile"));
            }

            // Tìm tài khoản trong cơ sở dữ liệu
            Optional<Account> accountOpt = accountService.findById(id);
            if (accountOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse("error", "Profile not found"));
            }

            Account account = accountOpt.get();

            // Cập nhật các trường thông tin
            if (name != null && !name.isBlank()) {
                account.setName(name);
            }

            if (password != null && !password.isBlank()) {
                if (password.length() < 6) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(createResponse("error", "Password must be at least 6 characters long"));
                }
                account.setPassword(passwordEncoder.encode(password));
            }

            if (phone != null && !phone.isBlank()) {
                if (!phone.matches("^[0-9]{10,11}$")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(createResponse("error", "Invalid phone number format"));
                }
                account.setPhone(phone);
            }

            if (email != null && !email.isBlank()) {
                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(createResponse("error", "Invalid email format"));
                }
                account.setEmail(email);
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = imageFile.getOriginalFilename();
                // TODO: Thêm logic lưu ảnh vào thư mục hoặc dịch vụ lưu trữ
                account.setImage(fileName);
            }

            // Lưu thay đổi vào cơ sở dữ liệu
            accountService.save(account);

            // Trả về phản hồi thành công
            return ResponseEntity.ok(createResponse("success", "Profile updated successfully"));

        } catch (Exception e) {
            // Log lỗi
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse("error", "An error occurred while updating the profile: " + e.getMessage()));
        }
    }

    private Map<String, Object> createResponse(String status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        return response;
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "ID of the account to be deleted") @PathVariable String id) {
        Optional<Account> accountOpt = accountService.findById(id);
        if (accountOpt.isPresent()) {
            accountService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
