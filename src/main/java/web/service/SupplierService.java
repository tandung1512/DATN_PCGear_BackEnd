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

    // Lấy danh sách tất cả các nhà cung cấp
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    // Lấy nhà cung cấp theo ID
    public Supplier getSupplierById(String id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Supplier with ID " + id + " not found"));
    }

    // Thêm nhà cung cấp mới
    public Supplier addSupplier(Supplier supplier) {
        // Kiểm tra xem id có hợp lệ không
        if (supplier.getId() == null || supplier.getId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier ID is required.");
        }

        // Kiểm tra nếu id đã tồn tại trong cơ sở dữ liệu
        if (supplierRepository.existsById(supplier.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Supplier with ID " + supplier.getId() + " already exists.");
        }

        // Kiểm tra nếu tên không hợp lệ
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier name is required.");
        }

        // Nếu tất cả các kiểm tra hợp lệ, lưu nhà cung cấp vào cơ sở dữ liệu
        return supplierRepository.save(supplier);
    }

    // Cập nhật nhà cung cấp theo ID
    public Supplier updateSupplierById(String id, Supplier supplierDetails) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Supplier with ID " + id + " not found"));

        // Cập nhật thông tin nhà cung cấp
        existingSupplier.setName(supplierDetails.getName() != null ? supplierDetails.getName() : existingSupplier.getName());
        existingSupplier.setPhoneNumber(supplierDetails.getPhoneNumber() != null ? supplierDetails.getPhoneNumber() : existingSupplier.getPhoneNumber());
        existingSupplier.setEmail(supplierDetails.getEmail() != null ? supplierDetails.getEmail() : existingSupplier.getEmail());
        existingSupplier.setAddress(supplierDetails.getAddress() != null ? supplierDetails.getAddress() : existingSupplier.getAddress());

        // Lưu cập nhật vào cơ sở dữ liệu
        return supplierRepository.save(existingSupplier);
    }

    
    // Xóa nhà cung cấp theo ID
    public void deleteSupplier(String id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Supplier with ID " + id + " not found");
        }

        supplierRepository.deleteById(id);
    }
}
