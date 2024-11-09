package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.model.Account;
import web.model.LoginRequest;
import web.model.responses.LoginResponse;
import web.service.AccountService;
import web.util.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Tìm kiếm tài khoản theo ID
        Optional<Account> accountOptional = accountService.findById(loginRequest.getId());

        // Kiểm tra tài khoản tồn tại và mật khẩu đúng
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            if (passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
                // Tạo JWT token khi đăng nhập thành công
                String token = jwtUtil.generateToken(account.getId());

                // Tạo LoginResponse để trả về thông tin người dùng và token
                LoginResponse response = new LoginResponse(
                        account.getId(),
                        account.getName(),
                        account.getPhone(),
                        account.getEmail(),
                        account.getAddress(),
                        token
                );

                return ResponseEntity.ok(response);
            }
        }

        // Trả về thông báo lỗi nếu tài khoản không tồn tại hoặc mật khẩu không đúng
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID or Password");
    }
}
