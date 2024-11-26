package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import web.model.Category;
import web.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy tất cả các danh mục
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    
    // Lấy danh mục theo ID
    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with ID: " + id));
    }

    // Thêm danh mục mới
    public Category createCategory(Category category) {
        // Kiểm tra xem ID đã được nhập chưa
        if (category.getId() == null || category.getId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category ID is required.");
        }

        // Kiểm tra nếu danh mục đã tồn tại với ID đã cho
        if (categoryRepository.existsById(category.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category with ID " + category.getId() + " already exists.");
        }

        // Kiểm tra tên danh mục có hợp lệ không
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name is required.");
        }

        // Kiểm tra xem ID có phải là giá trị hợp lệ không (ví dụ: dài tối đa 20 ký tự nếu ID là varchar(20))
        if (category.getId().length() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category ID cannot exceed 20 characters.");
        }

        // Nếu các thông tin hợp lệ, lưu vào cơ sở dữ liệu
        return categoryRepository.save(category);
    }
    
    // Cập nhật danh mục theo ID
    public Category updateCategory(String id, Category updatedCategory) {
        // Tìm kiếm category theo ID
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with ID: " + id));

        // Cập nhật các trường từ updatedCategory nếu có giá trị khác null
        if (updatedCategory.getName() != null) {
            existingCategory.setName(updatedCategory.getName());
        }
        if (updatedCategory.getDescription() != null) {
            existingCategory.setDescription(updatedCategory.getDescription());
        }
        // Cập nhật trường 'isHot' nếu có giá trị
        existingCategory.setIsHot(updatedCategory.getIsHot());

        // Lưu lại category đã cập nhật
        return categoryRepository.save(existingCategory);
        
        
    }
    
    public List<Category> getHotCategoriesWithProducts() {
        return categoryRepository.findHotCategoriesWithProducts();
    }


    // Xóa danh mục theo ID
    public void deleteCategory(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
    
    public List<Category> findByIsHotTrue() {
        return categoryRepository.findByIsHotTrue(); // Truy vấn danh mục có isHot = true
    }
    
    
}
