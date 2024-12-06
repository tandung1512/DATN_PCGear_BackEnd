package web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import web.service.GhnService;

@RestController
@RequestMapping("/api/ghn")
public class GhnController {


    @Autowired
    private GhnService ghnService;

    @GetMapping("/provinces")
    public ResponseEntity<Object> getProvinces() {
        return ghnService.getProvinces();
    }

    @PostMapping("/districts")
    public ResponseEntity<Object> getDistricts(@RequestBody Map<String, Object> requestBody) {
        Integer provinceId = (Integer) requestBody.get("province_id");
        return ghnService.getDistricts(provinceId);
    }

    @GetMapping("/wards")
    public ResponseEntity<Object> getWards(@RequestParam("district_id") Integer districtId) {
        return ghnService.getWards(districtId);
    }

    @PostMapping("/calculate-fee")
    public ResponseEntity<Object> calculateShippingFee(@RequestBody Map<String, Object> requestBody) {
        return ghnService.calculateShippingFee(requestBody);
    }

    @PostMapping("/create-order")
    public ResponseEntity<Object> createOrder(@RequestBody Map<String, Object> requestBody) {
    	
        return ghnService.createOrder(requestBody);
    }

    @GetMapping("/available-services")
    public ResponseEntity<Object> getAvailableServices(
    		 @RequestParam("shop_id") int shopId,
    		    @RequestParam("from_district") int fromDistrict,
    		    @RequestParam("to_district") int toDistrict){
        
        // Gọi phương thức lấy dịch vụ từ GHN
        return ghnService.getAvailableServices(shopId, fromDistrict, toDistrict);
    }




}
