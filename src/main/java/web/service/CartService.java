package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.Cart;
import web.repository.CartRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // Get all carts
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    // Get cart by ID
    public Cart getCartById(int id) {
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            return cart.get();
        } else {
            throw new RuntimeException("Cart not found with ID: " + id);
        }
    }

    // Add a new cart
    public Cart addCart(Cart cart) {
        return cartRepository.save(cart);
    }

    // Update cart by ID
    public Cart updateCartById(int id, Cart updatedCart) {
        Optional<Cart> existingCartOptional = cartRepository.findById(id);
        if (existingCartOptional.isPresent()) {
            Cart existingCart = existingCartOptional.get();
            existingCart.setQuantity(updatedCart.getQuantity());
            existingCart.setOrderDate(updatedCart.getOrderDate());
            existingCart.setStatus(updatedCart.getStatus());
//            existingCart.setProduct(updatedCart.getProduct());
            existingCart.setUser(updatedCart.getUser());
            return cartRepository.save(existingCart);
        } else {
            throw new RuntimeException("Cart not found with ID: " + id);
        }
    }

    // Delete cart by ID
    public void deleteCart(int id) {
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            cartRepository.delete(cart.get());
        } else {
            throw new RuntimeException("Cart not found with ID: " + id);
        }
    }
}
