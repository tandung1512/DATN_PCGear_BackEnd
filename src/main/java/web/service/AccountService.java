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
import java.util.UUID;
 
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    
    @Autowired
    private ServletContext servletContext;
    
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

    @Autowired
    private PasswordEncoder passwordEncoder; // Để mã hóa mật khẩu

    
 // Đăng ký tài khoản mới
    public void registerAccount(String id, String name, String password, String phone, String email, String address, MultipartFile imageFile) throws Exception {

        // Kiểm tra nếu ID (tên đăng nhập) đã tồn tại
    	 Optional<Account> existingAccount = accountRepository.findById(id);
         if (existingAccount.isPresent()) {
             throw new Exception("Username (ID) đã được sử dụng");
         }
         
         // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
         String encodedPassword = passwordEncoder.encode(password);


        // Xử lý lưu ảnh nếu có
        String imagePath = null;
        if (!imageFile.isEmpty()) {
            imagePath = saveImage(imageFile);  // Hàm lưu ảnh
        }

        // Tạo tài khoản mới với các thuộc tính mặc định
        Account newAccount = Account.builder()
                .id(id)
                .name(name)
                .password(encodedPassword)
                .phone(phone)
                .email(email)
                .address(address)
                .image(imagePath)
                .admin(false)
                .status(true)
                .confirm(true)
                .otp(null)
                .build();

        accountRepository.save(newAccount);
    }

    
    private String saveImage(MultipartFile imageFile) throws IOException {
        // Lấy đường dẫn lưu hình ảnh từ servlet context
        String uploadsDir = servletContext.getRealPath("/webapp/files/images/");

        // Tạo thư mục nếu chưa tồn tại
        Path uploadPath = Paths.get(uploadsDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        
        

        // Tạo tên file với ID ngẫu nhiên
        String fileName = imageFile.getOriginalFilename(); // Lấy tên file gốc
        Path imagePath = uploadPath.resolve(fileName); // Tạo đường dẫn file 

        // Sao chép file từ input stream
        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        return imagePath.toString(); // Trả về đường dẫn
    }
}
