package web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
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

    
    @Override
    public Invoice create(JsonNode orderData) {
        ObjectMapper mapper = new ObjectMapper();

        // Chuyển đổi dữ liệu orderData thành đối tượng Invoice
        Invoice inv = mapper.convertValue(orderData, Invoice.class);

        // Kiểm tra nếu có userId và tìm tài khoản người dùng
        if (orderData.has("userId") && !orderData.get("userId").isNull()) {
            Account user = accountRepository.findById(orderData.get("userId").asText()).orElse(null);
            inv.setUser(user);
        }

        // Lấy totalAmount từ frontend (orderData)
        if (orderData.has("totalAmount") && !orderData.get("totalAmount").isNull()) {
            double totalAmount = orderData.get("totalAmount").asDouble(); // Nhận giá trị từ frontend
            inv.setTotalAmount(totalAmount); // Cập nhật tổng tiền vào hóa đơn
        }

        // Lưu thông tin hóa đơn vào DB
        dao.save(inv);

        // Chuyển đổi dữ liệu items từ orderData thành List<DetailedInvoice>
        TypeReference<List<DetailedInvoice>> type = new TypeReference<List<DetailedInvoice>>() {};
        JsonNode itemsNode = orderData.get("detailedInvoices");

        // Kiểm tra nếu items không phải là null và không rỗng
        if (itemsNode != null && !itemsNode.isNull()) {
            List<DetailedInvoice> deList = mapper.convertValue(itemsNode, type);

            if (deList != null && !deList.isEmpty()) {
                for (DetailedInvoice d : deList) {
                    d.setInvoice(inv); // Liên kết chi tiết hóa đơn với hóa đơn

                    // Lấy thông tin sản phẩm từ product trong JSON và ánh xạ vào đối tượng Product
                    if (d.getProduct() != null && d.getProduct().getId() != null) {
                        Product product = productRepository.findById(d.getProduct().getId())
                                .orElseThrow(() -> new RuntimeException("Product not found"));
                        d.setProduct(product); // Liên kết chi tiết hóa đơn với sản phẩm
                    }

                    // Thêm phương thức thanh toán nếu có
                    if (orderData.has("paymentMethod") && !orderData.get("paymentMethod").isNull()) {
                        d.setPaymentMethod(orderData.get("paymentMethod").asText()); // Thêm phương thức thanh toán
                    }
                }

                // Lưu tất cả chi tiết hóa đơn vào DB
                ddao.saveAll(deList);
            }
        }

        return inv;
    }




    @Override
    public Invoice findById(String id) {
        return dao.findById(id).get();
    }

    @Override
    public List<Invoice> findByUsernameStatusPending(String username) {
      return dao.findByUsernameStatusPending(username);
    }

    @Override
    public List<Invoice> findByUsernameStatusDelivery(String username) {
         return dao.findByUsernameStatusDelivery(username);
    }

    @Override
    public List<Invoice> findByUsernameStatusComplete(String username) {
       return dao.findByUsernameStatusComplete(username);
    }

    @Override
    public List<Invoice> findByUsernameStatusCancelled(String username) {
        return dao.findByUsernameStatusCancelled(username);
    }
}
