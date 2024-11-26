package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import web.model.Addresses;

@Repository
public interface AddressReponsitory extends JpaRepository<Addresses, Long> {

}
