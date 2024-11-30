package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.model.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer>{

}
