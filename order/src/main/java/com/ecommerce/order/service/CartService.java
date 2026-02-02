package com.ecommerce.order.service;


import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;


    public boolean addToCart(String userId, CartItemRequest request) {
        //Look for Product
//        Optional<Product> productOpt = productRespository.findById(request.getProductId());
//        if(productOpt.isEmpty()){
//           return false;
//       }
//
//       Product product = productOpt.get();
//       if(product.getStockQuantity() < request.getQuantity()){
//           return false;
//       }
//
//       Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//       if(userOpt.isEmpty()){
//           return false;
//       }
//
//       User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId,request.getProductId());
        if(existingCartItem != null){
            //Update the existing cart
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        } else {
            //Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(Long.valueOf(userId));
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(cartItem);
        }

        return true;
    }

    public boolean deleteFromCart(String userId, String productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId,productId);

        if(cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
