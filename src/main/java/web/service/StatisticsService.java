package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import web.repository.InvoiceRepository;

@Service
public class StatisticsService {
	
	@Autowired
	InvoiceRepository invoiceRepository;
	
	public List<Map<String, Object>> getDailySales() {
        return invoiceRepository.getDailySales();
    }
}

