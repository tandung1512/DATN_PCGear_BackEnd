package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import web.model.Distinctive;
import web.repository.DistinctiveRepository;

import java.util.List;

@Service
public class DistinctiveService {

    @Autowired
    private DistinctiveRepository distinctiveRepository;

    // Lấy tất cả các distinctive
    public List<Distinctive> getAllDistinctives() {
        return distinctiveRepository.findAll();
    }

    // Lấy distinctive theo ID
    public Distinctive getDistinctiveById(String id) {
        return distinctiveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Distinctive with ID " + id + " not found"));
    }

    // Thêm mới distinctive
    public Distinctive addDistinctive(Distinctive distinctive) {
        // Kiểm tra nếu ID là null hoặc rỗng
        if (distinctive.getId() == null || distinctive.getId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Distinctive ID is required.");
        }

        // Kiểm tra nếu ID đã tồn tại
        if (distinctiveRepository.existsById(distinctive.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "Distinctive with ID " + distinctive.getId() + " already exists.");
        }

        // Kiểm tra nếu tên không hợp lệ
        if (distinctive.getName() == null || distinctive.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Distinctive name is required.");
        }

        // Lưu distinctive vào cơ sở dữ liệu
        return distinctiveRepository.save(distinctive);
    }

    // Cập nhật distinctive theo ID
    public Distinctive updateDistinctiveById(String id, Distinctive updatedDistinctive) {
        Distinctive existingDistinctive = distinctiveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Distinctive with ID " + id + " not found"));

        // Cập nhật các trường
        existingDistinctive.setName(updatedDistinctive.getName());
        existingDistinctive.setProductDistinctives(updatedDistinctive.getProductDistinctives());

        // Lưu thay đổi vào cơ sở dữ liệu
        return distinctiveRepository.save(existingDistinctive);
    }

    // Xóa distinctive theo ID
    public void deleteDistinctive(String id) {
        if (!distinctiveRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Distinctive with ID " + id + " not found");
        }
        distinctiveRepository.deleteById(id);
    }
}
