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
import web.model.Addresses;
import web.model.LoginRequest;
import web.model.responses.LoginResponse;
import web.service.AccountService;
import web.util.JwtUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            // Kiểm tra mật khẩu
            if (passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
                // Tạo JWT token
                String token = jwtUtil.generateToken(account.getId());

                // Chuyển đổi danh sách Addresses thành danh sách chuỗi
                List<String> addressStrings = account.getAddresses().stream()
                        .map(Addresses::getDetail) // Lấy thông tin địa chỉ từ đối tượng Addresses
                        .collect(Collectors.toList());

                // Tạo LoginResponse
                LoginResponse response = new LoginResponse(
                        account.getId(),
                        account.getName(),
                        account.getPhone(),
                        account.getEmail(),
                        addressStrings, // Truyền danh sách chuỗi địa chỉ
                        token
                );

                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

}
