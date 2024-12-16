package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

import web.service.StatisticsService;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*") 
public class StatisticsController {

	@Autowired
	StatisticsService statisticsService;
	
	@GetMapping("/monthly")
    public ResponseEntity<List<Map<String, Object>>> getMonthlySales(
            @RequestParam("month") int month) {
        List<Map<String, Object>> salesData = statisticsService.getSalesByMonths(month);
        return ResponseEntity.ok(salesData);
    }
	
	@GetMapping("/yearly")
    public ResponseEntity<List<Map<String, Object>>> getYearlySales() {
        List<Map<String, Object>> salesData = statisticsService.getSalesByYears();
        return ResponseEntity.ok(salesData);
    }
	
	@GetMapping
    public Map<String, Long> getStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("productCount", statisticsService.getProductCount());
        stats.put("userCount", statisticsService.getUserCount());
        stats.put("categoryCount", statisticsService.getCategoryCount());
        return stats;
    }
}
