package web.controller;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import web.service.OrderService;
import web.model.DetailedInvoice;
import web.model.Invoice;
import web.repository.DetailedInvoiceRepository;
import web.repository.InvoiceRepository;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class InvoiceRestController extends HttpServlet {
	@Autowired
	InvoiceRepository dao;
	@Autowired
	DetailedInvoiceRepository detailedInvoiceRepository;
	@Autowired
	OrderService ordersv;

	@GetMapping("/invoices")
	public ResponseEntity<List<Invoice>> getAll(Model model) {
		return ResponseEntity.ok(dao.findAll());
	}

	// Lấy hóa đơn theo keyword (status)
    @GetMapping("/invoices/{keyword}")
    public ResponseEntity<List<Invoice>> getInvoicesByKeyword(@PathVariable("keyword") String keyword) {
        List<Invoice> invoices = dao.findByStatusContainingIgnoreCase(keyword);
        return ResponseEntity.ok(invoices);
    }

	@GetMapping("/invoice/{id}")
	public ResponseEntity<Invoice> getOne(@PathVariable("id") String id) {
		// check xem id cs tồn tại trong cơ sở dữ liệu hay không trả về true or false
		if (!dao.existsById(id)) {
			return ResponseEntity.notFound().build();

		}
		return ResponseEntity.ok(dao.findById(id).get());
	}

	@PostMapping("/invoice")
	// đưa dữ liệu consumer lên rest API @requesstBody
	public ResponseEntity<Invoice> post(@RequestBody Invoice invoice) {
		if (dao.existsById(invoice.getId())) {
			return ResponseEntity.badRequest().build();
		}
		dao.save(invoice);
		return ResponseEntity.ok(invoice);
	}

	@PutMapping("/invoice/{id}")
	public ResponseEntity<Invoice> put(@PathVariable("id") String id, @RequestBody Invoice invoice) {
		if (!dao.existsById(id /* invoice.getId() */)) {
			return ResponseEntity.notFound().build();
		}
		dao.save(invoice);
		return ResponseEntity.ok(invoice);
	}

	@DeleteMapping("/invoice/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") String id) {
		if (!dao.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		dao.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/orders")
	public Invoice create(@RequestBody JsonNode orderData) {
		return ordersv.create(orderData);
	}

	@GetMapping("/ordered-list/details/{id}")
	public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable("id") String id) {
	    // Tìm hóa đơn theo ID
	    Invoice order = dao.findById(id).orElse(null);

	    if (order == null) {
	        // Nếu không tìm thấy hóa đơn, trả về 404
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Hóa đơn không tồn tại"));
	    }

	    // Lấy toàn bộ chi tiết hóa đơn từ hóa đơn chính
	    List<DetailedInvoice> detailedInvoices = order.getDetailedInvoices();

	    // Kiểm tra xem có chi tiết hóa đơn không
	    if (detailedInvoices == null || detailedInvoices.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Không có chi tiết hóa đơn cho mã hóa đơn này"));
	    }

	    // Tạo phản hồi trả về thông tin của tất cả các chi tiết hóa đơn
	    Map<String, Object> response = new HashMap<>();
	    response.put("orderId", order.getId());
	    response.put("address", order.getAddress());
	    response.put("status", order.getStatus());
	    response.put("orderDate", order.getOrderDate());
	    
	    // Tạo danh sách chi tiết hóa đơn
	    List<Map<String, Object>> detailedList = new ArrayList<>();
	    
	    for (DetailedInvoice detailedInvoice : detailedInvoices) {
	        Map<String, Object> detailInfo = new HashMap<>();
	        detailInfo.put("id", detailedInvoice.getId());
	        detailInfo.put("product_id", detailedInvoice.getProduct().getId());
	        detailInfo.put("product_name", detailedInvoice.getProduct().getName());
	        detailInfo.put("quantity", detailedInvoice.getQuantity());
	        detailInfo.put("price", detailedInvoice.getProduct().getPrice());
	        detailInfo.put("payment_method", detailedInvoice.getPaymentMethod());
	        
	        detailedList.add(detailInfo);
	    }
	    
	    // Đưa chi tiết hóa đơn vào phản hồi
	    response.put("detailedInvoices", detailedList);

	    return ResponseEntity.ok(response);
	}





	@PutMapping("/ordered-list/details/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable("id") String id) {
		Invoice order = ordersv.findById(id);

		if (order == null) {
			return ResponseEntity.notFound().build();
		}

		if (!"pending".equals(order.getStatus())) {
			return ResponseEntity.badRequest().body("Cannot cancel order with current status.");
		}

		
		order.setStatus("cancelled");
		// Update any other necessary properties of the order

		// Save the updated order
		dao.save(order);

		return ResponseEntity.ok("Order cancelled successfully.");
	}

	 @GetMapping("/order-list/pending")
	    public List<Invoice> getOrderedListPending(HttpServletRequest request) {
	        String username = request.getRemoteUser();
	        return ordersv.getInvoicesByUsernameAndStatus(username, "pending");
	    }
	 @GetMapping("/order-list/delivery")
	    public List<Invoice> getOrderedListDelivery() {
	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        return ordersv.getInvoicesByUsernameAndStatus(username, "delivery");
	    }

	 @GetMapping("/order-list/complete")
	    public List<Invoice> getOrderedListComplete(HttpServletRequest request) {
	        String username = request.getRemoteUser();
	        return ordersv.getInvoicesByUsernameAndStatus(username, "complete");
	    }

	 @GetMapping("/order-list/cancelled")
	    public List<Invoice> getOrderedListCancelled(HttpServletRequest request) {
	        String username = request.getRemoteUser();
	        return ordersv.getInvoicesByUsernameAndStatus(username, "cancelled");
	    }
	

}
