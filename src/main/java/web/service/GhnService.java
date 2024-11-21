package web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;  // Sử dụng jakarta thay vì javax
import java.util.HashMap;
import java.util.Map;

@Service
public class GhnService {

    // Inject token và API URL từ application.properties
    @Value("${ghn.token}")
    private String token;

    @Value("${ghn.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    // Constructor để inject RestTemplate
    public GhnService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // In token và API URL ra console khi ứng dụng khởi động
    @PostConstruct
    public void init() {
        System.out.println("GHN Token: " + token);
        System.out.println("GHN API URL: " + apiUrl);
    }

    // Cấu hình headers cho request
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token); // Thêm token vào headers
        return headers;
    }

    // Lấy danh sách tỉnh/thành phố
    public ResponseEntity<String> getProvinces() {
        String url = apiUrl + "/master-data/province";
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // Lấy danh sách quận/huyện theo tỉnh
    public ResponseEntity<String> getDistricts(int provinceId) {
        String url = apiUrl + "/master-data/district";
        Map<String, Integer> body = new HashMap<>();
        body.put("province_id", provinceId);

        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(body, getHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    // Lấy danh sách phường/xã theo quận
    public ResponseEntity<String> getWards(int districtId) {
        String url = apiUrl + "/master-data/ward";
        Map<String, Integer> body = new HashMap<>();
        body.put("district_id", districtId);

        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(body, getHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    // Tính phí vận chuyển
    public ResponseEntity<String> calculateShipping(Map<String, Object> requestData) {
        String url = apiUrl + "/v2/shipping-order/fee";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, getHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
