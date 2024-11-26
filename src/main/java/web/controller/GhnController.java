package web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import web.service.GhnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ghn")
@Tag(name = "GHN API", description = "Các API để tích hợp với hệ thống GHN")
public class GhnController {

    private final GhnService ghnService;

    // Constructor để inject GhnService
    public GhnController(GhnService ghnService) {
        this.ghnService = ghnService;
    }

    @Operation(summary = "Lấy danh sách tỉnh/thành phố", description = "Trả về danh sách các tỉnh và thành phố từ GHN")
    @GetMapping("/provinces")
    public ResponseEntity<?> getProvinces() {
        try {
            // Gọi service để lấy danh sách tỉnh
            return ghnService.getProvinces();
        } catch (Exception e) {
            // Nếu có lỗi xảy ra khi lấy danh sách tỉnh
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách tỉnh: " + e.getMessage());
        }
    }

    @Operation(summary = "Lấy danh sách quận/huyện theo tỉnh", description = "Trả về danh sách các quận/huyện thuộc tỉnh được chỉ định bằng province_id")
    @PostMapping("/districts")
    public ResponseEntity<?> getDistricts(@RequestBody Map<String, Integer> request) {
        Integer provinceId = request.get("province_id");

        // Kiểm tra xem province_id có hợp lệ không
        if (provinceId == null) {
            return ResponseEntity.status(400).body("Lỗi: province_id không được cung cấp.");
        }

        try {
            // Gọi service để lấy danh sách quận/huyện theo province_id
            return ghnService.getDistricts(provinceId);
        } catch (Exception e) {
            // Nếu có lỗi xảy ra khi lấy danh sách quận
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách quận: " + e.getMessage());
        }
    }

    @Operation(summary = "Lấy danh sách phường/xã theo quận", description = "Trả về danh sách các phường/xã thuộc quận được chỉ định bằng district_id")
    @PostMapping("/wards")
    public ResponseEntity<?> getWards(@RequestBody Map<String, Integer> request) {
        Integer districtId = request.get("district_id");

        // Kiểm tra xem district_id có hợp lệ không
        if (districtId == null) {
            return ResponseEntity.status(400).body("Lỗi: district_id không được cung cấp.");
        }

        try {
            // Gọi service để lấy danh sách phường/xã theo district_id
            return ghnService.getWards(districtId);
        } catch (Exception e) {
            // Nếu có lỗi xảy ra khi lấy danh sách phường/xã
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách phường: " + e.getMessage());
        }
    }

    @Operation(summary = "Tính phí vận chuyển", description = "Tính phí vận chuyển giữa các địa điểm với thông tin đầu vào được cung cấp")
    @PostMapping("/calculate-shipping")
    public ResponseEntity<?> calculateShipping(@RequestBody Map<String, Object> request) {
        // Kiểm tra xem request có thông tin đầy đủ không
        if (request.isEmpty()) {
            return ResponseEntity.status(400).body("Lỗi: Dữ liệu tính phí vận chuyển không hợp lệ.");
        }

        try {
            // Gọi service để tính phí vận chuyển
            return ghnService.calculateShipping(request);
        } catch (Exception e) {
            // Nếu có lỗi xảy ra khi tính phí vận chuyển
            return ResponseEntity.status(500).body("Lỗi khi tính phí vận chuyển: " + e.getMessage());
        }
    }
}
