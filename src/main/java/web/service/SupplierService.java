package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import web.model.Supplier;
import web.repository.SupplierRepository;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    // Lấy danh sách tất cả các supplier
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    // Lấy thông tin supplier theo ID
    public Supplier getSupplierById(String id) {
        // Kiểm tra nếu supplier không tồn tại thì trả về lỗi 404
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Supplier with ID " + id + " not found"));
    }

    public Supplier addSupplier(Supplier supplier) {
        // Kiểm tra nếu tên không hợp lệ
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier name is required.");
        }

        // Kiểm tra nếu id đã tồn tại trong cơ sở dữ liệu
        if (supplier.getId() != null && !supplier.getId().trim().isEmpty()) {
            // If the ID is provided, check if it already exists
            if (supplierRepository.existsById(supplier.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                        "Supplier with ID " + supplier.getId() + " already exists.");
            }
        }

        // Nếu tất cả các kiểm tra hợp lệ, lưu supplier vào cơ sở dữ liệu
        return supplierRepository.save(supplier);  // Câu lệnh INSERT sẽ bao gồm cả id (nếu có)
    }

    // Xóa supplier theo ID
    public void deleteSupplier(String id) {
        // Kiểm tra nếu supplier không tồn tại
        if (!supplierRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Supplier with ID " + id + " not found");
        }

        // Xóa supplier theo ID
        supplierRepository.deleteById(id);
    }
}
