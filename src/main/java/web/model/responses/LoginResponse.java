package web.model.responses;

public class LoginResponse {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String token;

    public LoginResponse(String id, String name, String phone, String email, String address, String token) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.token = token;
    }

    // Getters và setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getToken() { return token; }
}
