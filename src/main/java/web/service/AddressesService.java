package web.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.model.Addresses;
import web.repository.AddressReponsitory;

import java.util.List;
@Service
public class AddressesService {
	  @Autowired
	    private AddressReponsitory addressRepository;

	    public List<Addresses> getAllAddresses() {
	        return addressRepository.findAll();
	    }
	    public Addresses saveAddress(Addresses address) {
	        return addressRepository.save(address);
	    }
}
