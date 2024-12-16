package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import web.repository.InvoiceRepository;
import web.repository.StatisticsRepository;

@Service
public class StatisticsService {
	
	@Autowired
	InvoiceRepository invoiceRepository;
	@Autowired
    private StatisticsRepository statisticsRepository;

	
	public List<Map<String, Object>> getSalesByMonths(int month) {
        return invoiceRepository.getMonthlySales(month);
    }
	
	public List<Map<String, Object>> getSalesByYears() {
        return invoiceRepository.getYearlySales();
    }
	
    public Long getProductCount() {
        return statisticsRepository.countProducts();
    }

    public Long getUserCount() {
        return statisticsRepository.countUsers();
    }
    
    public Long getCategoryCount() {
        return statisticsRepository.countCategories();
    }
}

