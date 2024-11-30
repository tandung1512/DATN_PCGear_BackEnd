package web.model.responses;

import java.util.List;

public class LoginResponse {
    private String id;
    private String name;
    private String phone;
    private String email;
    private List<String> addresses;
    private String token;

    public LoginResponse(String id, String name, String phone, String email, List<String> addresses, String token) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.addresses = addresses;
        this.token = token;
    }

    // Getters và setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public List<String> getAddresses() {
        return addresses;
    }
    public String getToken() { return token; }
}
