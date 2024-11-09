package web.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import web.model.Account;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Account account;

    public CustomUserDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trả về danh sách quyền dựa trên vai trò của tài khoản
        return List.of(new SimpleGrantedAuthority(account.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // Trả về mật khẩu của tài khoản
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        // Sử dụng ID tài khoản làm tên đăng nhập
        return account.getId(); 
    }

    @Override
    public boolean isAccountNonExpired() {
        // Có thể được điều chỉnh để kiểm tra thời gian hết hạn tài khoản nếu cần
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Có thể được điều chỉnh để kiểm tra xem tài khoản có bị khóa không
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Có thể được điều chỉnh để kiểm tra xem thông tin xác thực có hết hạn không
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Trả về trạng thái của tài khoản
        return account.isStatus(); 
    }
}
