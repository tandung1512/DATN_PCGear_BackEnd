package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.Brand;
import web.repository.BrandRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Brand getBrandById(String id) {
        Optional<Brand> brand = brandRepository.findById(id);
        return brand.orElse(null);  // Trả về null nếu không tìm thấy
    }

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public void deleteBrand(String id) {
        brandRepository.deleteById(id);
    }
}
