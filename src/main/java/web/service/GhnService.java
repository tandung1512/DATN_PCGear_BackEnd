package web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GhnService {

	private final String ghnApiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api";
    private final String token = "0e182c9b-abff-11ef-accc-c6f6f22065b5"; 
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Object> getProvinces() {
        String url = ghnApiUrl + "/master-data/province";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
    }

    public ResponseEntity<Object> getDistricts(Integer provinceId) {
        String url = ghnApiUrl + "/master-data/district";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);

        Map<String, Object> body = new HashMap<>();
        body.put("province_id", provinceId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
    }

    public ResponseEntity<Object> getWards(Integer districtId) {
        String url = ghnApiUrl + "/master-data/ward?district_id=" + districtId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
    }

    public ResponseEntity<Object> calculateShippingFee(Map<String, Object> requestBody) {
        String url = ghnApiUrl + "/v2/shipping-order/fee";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);  // Token của bạn từ GHN
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Gửi yêu cầu POST đến GHN API và nhận phản hồi
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Trả về dữ liệu từ GHN API nếu thành công
                return ResponseEntity.ok(response.getBody());
            } else {
                // Nếu GHN trả về lỗi (mã trạng thái không thành công)
                return ResponseEntity.status(response.getStatusCode())
                        .body(response.getBody());
            }
        } catch (RestClientException e) {
            // Xử lý lỗi khi gửi yêu cầu tới GHN (ví dụ như không kết nối được)
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to calculate shipping fee.");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    public ResponseEntity<Object> createOrder(Map<String, Object> requestBody) {
        String url = ghnApiUrl + "/v2/shipping-order/create";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // In dữ liệu request trước khi gửi đi
            System.out.println("Request to GHN API:");
            System.out.println("URL: " + url);
            System.out.println("Headers: " + headers);
            System.out.println("Body: " + requestBody);

            // Gửi yêu cầu và nhận phản hồi
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);

            // In dữ liệu phản hồi khi thành công
            System.out.println("Response from GHN API:");
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            return response;

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // In dữ liệu phản hồi khi có lỗi từ GHN
            System.out.println("HTTP Error from GHN API:");
            System.out.println("Status Code: " + ex.getStatusCode());
            System.out.println("Response Body: " + ex.getResponseBodyAsString());

            // Trả về phản hồi lỗi cho frontend
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            // In thông tin lỗi khác nếu có
            System.out.println("Unexpected Error:");
            ex.printStackTrace();

            // Trả về phản hồi lỗi không xác định cho frontend
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi không xác định: " + ex.getMessage());
        }
    }

    public ResponseEntity<Object> getAvailableServices(int shopId, int fromDistrict, int toDistrict) {
        // Cấu trúc URL với các tham số query
        String url = ghnApiUrl + "/v2/shipping-order/available-services?shop_id=" + shopId +
                     "&from_district=" + fromDistrict + "&to_district=" + toDistrict;

        // Thiết lập headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token); // Đảm bảo rằng token chính xác

        // Tạo HttpEntity chứa headers (không cần body cho GET request)
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            // Gửi GET request tới GHN
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

            // In thông tin phản hồi để kiểm tra
            System.out.println("Response status code: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());

            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // In lỗi nếu xảy ra
            System.err.println("Error response from GHN API: " + e.getResponseBodyAsString());
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return new ResponseEntity<>("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
