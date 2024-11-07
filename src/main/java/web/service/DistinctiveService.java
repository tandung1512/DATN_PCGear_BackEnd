package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.Distinctive;
import web.repository.DistinctiveRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DistinctiveService {

    @Autowired
    private DistinctiveRepository distinctiveRepository;

    public List<Distinctive> getAllDistinctives() {
        return distinctiveRepository.findAll();
    }

    public Distinctive getDistinctiveById(String id) {
        Optional<Distinctive> distinctive = distinctiveRepository.findById(id);
        return distinctive.orElse(null);
    }

    public Distinctive saveDistinctive(Distinctive distinctive) {
        return distinctiveRepository.save(distinctive);
    }

    public void deleteDistinctive(String id) {
        distinctiveRepository.deleteById(id);
    }
}
