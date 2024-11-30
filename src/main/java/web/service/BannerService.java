package web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import web.model.Banner;
import web.model.Brand;
import web.repository.BannerRepository;

@Service
public class BannerService {
	
	@Autowired
	private BannerRepository bannerRepository;
	 public List<Banner> getAllBanner() {
	        return bannerRepository.findAll();
	    }

	    // Lấy banner theo ID
	    public Banner getBannerById(int id) {
	        return bannerRepository.findById(id)
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found with ID: " + id));
	    }
	    
	 // Tạo mới Banner
	    public Banner createBanner(Banner banner) {
	        return bannerRepository.save(banner);
	    }
	    
	 // Cập nhật banner
	    public Banner updateBanner(Banner banner) {
	        return bannerRepository.save(banner);
	    }
	    
	 // Xóa thương hiệu theo ID
	    public void deleteBanner(int id) {
	        if (!bannerRepository.existsById(id)) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Banner not found with ID: " + id);
	        }
	        bannerRepository.deleteById(id);
	    }
	
}
