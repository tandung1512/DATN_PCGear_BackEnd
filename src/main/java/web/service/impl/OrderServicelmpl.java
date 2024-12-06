package web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.service.OrderService;
import web.model.Account;
import web.model.DetailedInvoice;
import web.model.Invoice;
import web.model.Product;
import web.repository.AccountRepository;
import web.repository.DetailedInvoiceRepository;
import web.repository.InvoiceRepository;
import web.repository.ProductRepository;

@Service
public class OrderServicelmpl implements OrderService {
    @Autowired
    InvoiceRepository dao;
    @Autowired
    DetailedInvoiceRepository ddao;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    
    @Override
    public Invoice create(JsonNode orderData) {
        ObjectMapper mapper = new ObjectMapper();

        // Chuyển đổi dữ liệu orderData thành đối tượng Invoice
        Invoice inv = mapper.convertValue(orderData, Invoice.class);

        // Liên kết hóa đơn với user nếu có
        if (orderData.has("userId") && !orderData.get("userId").isNull()) {
            Account user = accountRepository.findById(orderData.get("userId").asText()).orElse(null);
            inv.setUser(user);
        }

        // Lấy tổng tiền từ frontend nếu có
        if (orderData.has("totalAmount") && !orderData.get("totalAmount").isNull()) {
            double totalAmount = orderData.get("totalAmount").asDouble();
            inv.setTotalAmount(totalAmount);
        }

        // Lưu hóa đơn vào DB
        dao.save(inv);

        
        JsonNode itemsNode = orderData.get("detailedInvoices");
        if (itemsNode != null && !itemsNode.isNull()) {
            for (JsonNode itemNode : itemsNode) {
            
                DetailedInvoice detailedInvoice = mapper.convertValue(itemNode, DetailedInvoice.class);

               
                detailedInvoice.setInvoice(inv);
                Product product = productRepository.findById(detailedInvoice.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                detailedInvoice.setProduct(product);
                System.out.println(orderData.toPrettyString());

                Optional<DetailedInvoice> existingDetail = ddao.findByInvoiceAndProduct(inv, detailedInvoice.getProduct());
                if (existingDetail.isPresent()) {
                   
                    DetailedInvoice detailToUpdate = existingDetail.get();
                    if (detailToUpdate.getQuantity() != detailedInvoice.getQuantity()) {
                        detailToUpdate.setQuantity(detailedInvoice.getQuantity());
                        ddao.save(detailToUpdate);
                    }
                } else {
                   
                    ddao.save(detailedInvoice);
                }

            }
        }

        return inv;
    }






    @Override
    public Invoice findById(String id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    @Override
    public List<Invoice> getInvoicesByUsernameAndStatus(String username, String status) {
        return invoiceRepository.findByUsernameAndStatus(username, status);
    }
}
