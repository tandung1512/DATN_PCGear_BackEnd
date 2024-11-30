package web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import web.model.Account;
import web.model.Addresses;
import web.service.AccountService;
import web.service.AddressesService;

@RestController
@RequestMapping("/api/accounts/{userId}/addresses")
public class AddressesController {

    @Autowired
    private AddressesService addressService;
    @Autowired
    private AccountService accountService;

    
    @GetMapping
    public List<Addresses> getAllAddresses() {
        return addressService.getAllAddresses();
    }
    @PostMapping
    public ResponseEntity<?> addNewAddress(
            @PathVariable String userId,
            @RequestBody Addresses address) {

        // Tìm tài khoản theo userId
        Optional<Account> accountOptional = accountService.findById(userId);
        if (accountOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account with ID " + userId + " not found.");
        }

        // Liên kết địa chỉ với tài khoản
        Account account = accountOptional.get();
        address.setAccount(account); // Gán tài khoản vào địa chỉ

        // Lưu địa chỉ
        Addresses savedAddress = addressService.saveAddress(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }

}