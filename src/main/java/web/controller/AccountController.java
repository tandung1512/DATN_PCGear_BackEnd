package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import web.model.Account;
import web.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Lấy danh sách tất cả các tài khoản 
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        return ResponseEntity.ok(accounts);
    }

    // Lấy tài khoản theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable String id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // Đăng ký tài khoản người dùng
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam String email,
            @RequestParam String address,
            @RequestParam("image") MultipartFile image) {

        try {
            // Mã hóa mật khẩu trước khi lưu
            String encodedPassword = passwordEncoder.encode(password);
            accountService.registerAccount(id, name, encodedPassword, phone, email, address, image);
            return ResponseEntity.ok("Account registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Tạo tài khoản mới
    @PostMapping
    public ResponseEntity<?> createAccount(
            @RequestParam("id") String id,
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("admin") boolean admin,
            @RequestParam(value = "status", defaultValue = "true") boolean status,
            @RequestParam(value = "confirm", defaultValue = "true") boolean confirm) {
        
        try {
            System.out.println("Đang xử lý yêu cầu tạo tài khoản mới...");

            String encodedPassword = passwordEncoder.encode(password);
            
            Account account = new Account();
            account.setId(id);
            account.setName(name);
            account.setPassword(encodedPassword);
            account.setPhone(phone);
            account.setEmail(email);
            account.setAddress(address);
            account.setAdmin(admin);
            account.setStatus(status);
            account.setConfirm(confirm);

            // Kiểm tra và lưu thông tin ảnh
            if (image != null && !image.isEmpty()) {
                String fileName = image.getOriginalFilename();
                account.setImage(fileName);
                // TODO: Lưu file ảnh vào một thư mục trên server hoặc xử lý theo yêu cầu của bạn
            } else {
                account.setImage(null);
            }

           Account createdAccount = accountService.save(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo tài khoản: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể tạo tài khoản: " + e.getMessage());
        }
    }
    
    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(
            @PathVariable String id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(value = "admin", required = false) Boolean admin,
            @RequestParam(value = "status", required = false) Boolean status) {

        try {
            // Tìm tài khoản theo ID
            Account existingAccount = accountService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

            // Cập nhật thông tin tài khoản nếu các trường không null
            if (name != null) existingAccount.setName(name);
            if (password != null) existingAccount.setPassword(passwordEncoder.encode(password));;
            if (phone != null) existingAccount.setPhone(phone);
            if (email != null) existingAccount.setEmail(email);
            if (address != null) existingAccount.setAddress(address);
            if (admin != null) existingAccount.setAdmin(admin);
            if (status != null) existingAccount.setStatus(status);

            // Nếu có hình ảnh mới, xử lý việc lưu ảnh
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = imageFile.getOriginalFilename();
                existingAccount.setImage(fileName); // Cập nhật đường dẫn hình ảnh
                // TODO: Lưu file ảnh vào một thư mục trên server hoặc xử lý theo yêu cầu của bạn
            }

            Account updatedAccount = accountService.save(existingAccount);
            return ResponseEntity.ok(updatedAccount);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage()); // Trả về thông báo khi không tìm thấy tài khoản
        } catch (Exception e) {
            // Xử lý lỗi khác
            System.err.println("Lỗi khi cập nhật tài khoản: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi cập nhật tài khoản: " + e.getMessage());
        }
    }




    // Xóa tài khoản theo ID
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
