package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import web.service.StatisticsService;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

	@Autowired
	StatisticsService statisticsService;
	
	@GetMapping("/daily-sales")
    public ResponseEntity<List<Map<String, Object>>> getDailySales() {
        List<Map<String, Object>> salesData = statisticsService.getDailySales();
        return ResponseEntity.ok(salesData);
    }
}
