package web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.service.OrderService;
import web.model.DetailedInvoice;
import web.model.Invoice;
import web.repository.DetailedInvoiceRepository;
import web.repository.InvoiceRepository;

@Service
public class OrderServicelmpl implements OrderService {
    @Autowired
    InvoiceRepository dao;
    @Autowired
    DetailedInvoiceRepository ddao;

    @Override
    public Invoice create(JsonNode orderData) {
        ObjectMapper mapper = new ObjectMapper();

        Invoice inv = mapper.convertValue(orderData, Invoice.class);
        dao.save(inv);

        TypeReference<List<DetailedInvoice>> type = new TypeReference<List<DetailedInvoice>>() {
        };
        List<DetailedInvoice> deList = mapper.convertValue(orderData.get("detailedInvoices"), type);
        if (deList != null) {
            deList.forEach(d -> d.setInvoice(inv));
            ddao.saveAll(deList);
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
