package web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
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
import web.model.Invoice;

import web.model.MonthlySalesStatistics;
import web.repository.InvoiceRepository;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class InvoiceRestController extends HttpServlet {
	@Autowired
	InvoiceRepository dao;

	@Autowired
	OrderService odersv;

	@GetMapping("/invoices")
	public ResponseEntity<List<Invoice>> getAll(Model model) {
		return ResponseEntity.ok(dao.findAll());
	}

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
		return odersv.create(orderData);
	}

	@GetMapping("/ordered-list/details/{id}")
	public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable("id") String id) {
	    Invoice order = odersv.findById(id);

	    // Kiểm tra chi tiết sản phẩm trong hóa đơn
	    if (order.getDetailedInvoices() == null || order.getDetailedInvoices().isEmpty()) {
	        System.out.println("Không có chi tiết sản phẩm trong hóa đơn: " + id);
	    } else {
	        System.out.println("Chi tiết sản phẩm: " + order.getDetailedInvoices());
	    }

	    double totalPrice = order.getDetailedInvoices().stream()
	            .mapToDouble(detail -> detail.getProduct().getPrice() * detail.getQuantity()).sum();

	    Map<String, Object> response = new HashMap<>();
	    response.put("order", order);
	    response.put("totalPrice", totalPrice);

	    return ResponseEntity.ok(response);
	}


	@PutMapping("/ordered-list/details/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable("id") String id) {
		Invoice order = odersv.findById(id);

		if (order == null) {
			return ResponseEntity.notFound().build();
		}

		if (!"pending".equals(order.getStatus())) {
			return ResponseEntity.badRequest().body("Cannot cancel order with current status.");
		}

		// Update the order status to "cancelled" (or update status as needed)
		order.setStatus("cancelled");
		// Update any other necessary properties of the order

		// Save the updated order
		dao.save(order);

		return ResponseEntity.ok("Order cancelled successfully.");
	}

	@GetMapping("/order-list/pending")
	public List<Invoice> getOrderedList(Model model, HttpServletRequest request) {
		String username = request.getRemoteUser();
		List<Invoice> orders = odersv.findByUsernameStatusPending(username);
		return orders;
	}

	@GetMapping("/order-list/delivery")
	public List<Invoice> getOrderedListdelivery(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    List<Invoice> orders = odersv.findByUsernameStatusDelivery(username);
	    return orders;
	}

	@GetMapping("/order-list/complete")
	public List<Invoice> getOrderedListComplete(Model model, HttpServletRequest request) {
		String username = request.getRemoteUser();
		List<Invoice> orders = odersv.findByUsernameStatusComplete(username);
		return orders;
	}

	@GetMapping("/order-list/cancelled")
	public List<Invoice> getOrderedListCacelled(Model model, HttpServletRequest request) {
		String username = request.getRemoteUser();
		List<Invoice> orders = odersv.findByUsernameStatusCancelled(username);
		return orders;
	}
	

}
