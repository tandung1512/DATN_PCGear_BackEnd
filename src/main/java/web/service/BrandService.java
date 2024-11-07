package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import web.model.Brand;
import web.repository.BrandRepository;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    // Lấy tất cả thương hiệu
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    // Lấy thương hiệu theo ID
    public Brand getBrandById(String id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found with ID: " + id));
    }

    // Thêm thương hiệu mới
    public Brand addBrand(Brand brand) {
        // Kiểm tra xem id đã được nhập chưa
        if (brand.getId() == null || brand.getId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brand ID is required.");
        }

        // Kiểm tra nếu thương hiệu đã tồn tại với ID đã cho
        if (brandRepository.existsById(brand.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brand with ID " + brand.getId() + " already exists.");
        }

        // Kiểm tra các thông tin khác như tên thương hiệu có hợp lệ không
        if (brand.getName() == null || brand.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brand name is required.");
        }

        // Kiểm tra xem ID có phải là giá trị hợp lệ không (ví dụ: dài tối đa 20 ký tự nếu ID là varchar(20))
        if (brand.getId().length() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brand ID cannot exceed 20 characters.");
        }

        // Nếu các thông tin hợp lệ, lưu vào cơ sở dữ liệu
        return brandRepository.save(brand);
    }

    // Xóa thương hiệu theo ID
    public void deleteBrand(String id) {
        if (!brandRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found with ID: " + id);
        }
        brandRepository.deleteById(id);
    }
}
