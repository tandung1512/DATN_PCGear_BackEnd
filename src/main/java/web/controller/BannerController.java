package web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Parameter;
import web.controller.BrandController.BrandErrorResponse;
import web.model.Account;
import web.model.Banner;
import web.repository.BannerRepository;
import web.service.BannerService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/banners")
public class BannerController {

	@Autowired
	private BannerService bannerService;
	
	@Autowired
	private BannerRepository bannerRepository;

	private final String UPLOAD_DIR = "src/main/resources/webapp/files/images/"; // Thư mục lưu trữ ảnh

	// API trả về hình ảnh
	@GetMapping("/images/{img}")
	public ResponseEntity<Resource> getImage(@PathVariable("img") String img) {
		// Đảm bảo rằng đường dẫn file ảnh hợp lệ
		Path imagePath = Paths.get("src/main/resources/webapp/files/images/").resolve(img);
		Resource resource = new FileSystemResource(imagePath);

		if (resource.exists() && resource.isReadable()) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG) // Hoặc MediaType.IMAGE_PNG nếu là ảnh PNG
					.body(resource);
		} else {
			return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy ảnh
		}
	}

	

	@GetMapping
	public ResponseEntity<List<Banner>> getAllBanner() {
		List<Banner> banners = bannerService.getAllBanner();
		System.out.print(banners);
		if (banners.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(banners); // Return 204 if no brands found
		}
		return ResponseEntity.ok(banners); // Return 200 with list of brands
	}

	// Tạo Banner kèm hình ảnh
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<Banner> createBanner(@RequestParam("img") MultipartFile img,
			@RequestParam("link") String link, @RequestParam("isActive") boolean isActive,
			@RequestParam("isBlank") boolean isBlank) {
		String imgPath = null;
		// Xử lý tải lên hình ảnh
		if (img != null) {
			imgPath = saveImage(img);
		}

		// Tạo đối tượng Banner
		Banner banner = new Banner();
		banner.setImg(imgPath);
		banner.setLink(link);
		banner.setActive(isActive);
		banner.setBlank(isBlank);

		// Lưu Banner vào cơ sở dữ liệu
		Banner savedBanner = bannerService.createBanner(banner);
		return ResponseEntity.ok(savedBanner);
	}

	@PutMapping("/{stt}")
	public ResponseEntity<Banner> updateBanner(@PathVariable int stt, @RequestParam(required = false) MultipartFile img,
			@RequestParam(required = false) String link, @RequestParam(required = false) Boolean isActive,
			@RequestParam(required = false) Boolean isBlank) {

		try {

			Banner banner = bannerService.getBannerById(stt);

			String imgPath = null;
			// Xử lý tải lên hình ảnh
			if (img != null) {
				imgPath = saveImage(img);
				banner.setImg(imgPath);
				System.out.print(imgPath);
			}

			// Cập nhật các trường không phải là ảnh
			if (link != null) {
				banner.setLink(link);
			}
			if (isActive != null) {
				banner.setActive(isActive);
			}
			if (isBlank != null) {
				banner.setBlank(isBlank);
			}

			// Cập nhật banner trong cơ sở dữ liệu
			Banner updatedBanner = bannerService.updateBanner(banner);
			return ResponseEntity.ok(updatedBanner);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

		}

	}
	
	@PatchMapping("/{stt}")
	public ResponseEntity<?> updateIsActive(@PathVariable int stt, @RequestBody Map<String, Object> updates) {
	    Optional<Banner> existingBanner = bannerRepository.findById(stt);
	    if (existingBanner.isPresent()) {
	        Banner banner = existingBanner.get();
	        if (updates.containsKey("isActive")) {
	            banner.setActive((Boolean) updates.get("isActive"));
	        }
	        bannerRepository.save(banner);
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	@DeleteMapping("/{stt}")
	public ResponseEntity<?> deleteBanner(@PathVariable int stt) {
		try {
			bannerService.deleteBanner(stt); // Attempt to delete the brand
			return ResponseEntity.ok("Banner deleted successfully"); // Return 200 if deletion successful
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(new BrandErrorResponse(e.getReason())); // Handle error
		}
	}

	@GetMapping("/{stt}")
	public ResponseEntity<Banner> getBanner(@PathVariable("stt") Integer stt) {
		Banner banner = bannerService.getBannerById(stt);
		return ResponseEntity.ok(banner);
	}

	// Hàm lưu ảnh vào thư mục và trả về đường dẫn

	private String saveImage(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		File dir = new File(UPLOAD_DIR);
		if (!dir.exists())
			dir.mkdirs();

		String filePath = Paths.get(UPLOAD_DIR, fileName).toString();
		try (FileOutputStream fos = new FileOutputStream(filePath)) {
			fos.write(file.getBytes());

			// Trả về URL truy cập ảnh qua API
			return MvcUriComponentsBuilder.fromMethodName(BannerController.class, "getImage", fileName).build()
					.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
