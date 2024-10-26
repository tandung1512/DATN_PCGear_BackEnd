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

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Để so sánh mật khẩu đã mã hóa

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Tìm tài khoản theo ID
        Optional<Account> accountOptional = accountService.findById(loginRequest.getId());

        // Kiểm tra nếu tài khoản tồn tại và mật khẩu trùng khớp
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            // So sánh mật khẩu đã mã hóa với mật khẩu người dùng nhập vào
            if (passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
                // Trả về thông tin tài khoản đầy đủ nếu đăng nhập thành công
            	
            	// tạo token bằng jwt
            	
            	// tạo mới loginResponse object
            	LoginResponse data = new LoginResponse();
            	data.id= account.getId();
            	data.name= account.getName();
            	data.phone= account.getPhone();
            	data.email=account.getEmail();
            	data.address = account.getAddress();
            	data.token= "";
            	
                return ResponseEntity.ok(data);
            }
        }

        // Trả về lỗi nếu thông tin đăng nhập không chính xác
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Thông tin đăng nhập không chính xác");
    }
}
