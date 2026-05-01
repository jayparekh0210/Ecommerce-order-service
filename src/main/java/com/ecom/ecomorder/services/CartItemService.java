package com.ecom.ecomorder.services;

import com.ecom.ecomorder.adapters.CartItemConverter;
import com.ecom.ecomorder.dto.requests.CartRequest;
import com.ecom.ecomorder.dto.responses.CartResponse;
import com.ecom.ecomorder.models.CartItem;
import com.ecom.ecomorder.repositories.CartItemRepository;
import com.ecom.ecomorder.validations.CartValidation;
import lombok.Data;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Data
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartValidation cartValidation;
    private final CartItemConverter cartItemConverter;

    public boolean addToCart(String userId, CartRequest cartRequest) {
        String productId = String.valueOf(cartRequest.getProductId());

        if (cartValidation.isAddToCartValid(userId, productId)) {
            CartItem exsistingCartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
            if (exsistingCartItem != null) {
                if (cartValidation.AddToExistingCartValid(productId)) {
                    addMoreQuantityToCart(exsistingCartItem, cartRequest);
                } else {
                    return false;
                }
            } else {
                addNewCartItem(userId, productId, cartRequest);
            }
            return true;
        }
        return false;
    }

    public boolean deleteCartItem(String userid, Long productId) {

        if (cartValidation.isRemoveFromCartValid(userid, String.valueOf(productId))) {
            CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userid, String.valueOf(productId));
            if (cartItem != null) {
                cartItemRepository.delete(cartItem);
                return true;
            }
        }
        return false;
    }

    public boolean removeFromCart(String userid, Long productId) {
        if (cartValidation.isRemoveFromCartValid(userid, String.valueOf(productId))) {
            CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userid, String.valueOf(productId));
            if (cartItem != null) {
                if (cartItem.getQuantity() > 1) {
                    decreaseQuantityFromCart(cartItem);
                } else {
                    cartItemRepository.delete(cartItem);
                }
                return true;
            }
        }
        return false;
    }

    public List<CartResponse> getCartItems(String userid){
        return  cartItemRepository.findCartItemsByUserId(userid)
                        .stream()
                        .map(cartItemConverter::cartItemModelToCartResponse)
                        .toList();

    }

    private void decreaseQuantityFromCart(CartItem cartItem) {
        BigDecimal singleItemPrice = cartItem.getPrice()
                .divide(BigDecimal.valueOf(cartItem.getQuantity()), 2, RoundingMode.HALF_UP);
        cartItem.setQuantity(cartItem.getQuantity() - 1);
        cartItem.setPrice(singleItemPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        cartItemRepository.save(cartItem);
    }

    private void addMoreQuantityToCart(CartItem exsistingCartItem, CartRequest cartRequest) {
        BigDecimal singleItemPrice = exsistingCartItem.getPrice()
                .divide(BigDecimal.valueOf(exsistingCartItem.getQuantity()), 2, RoundingMode.HALF_UP);
        exsistingCartItem.setQuantity(exsistingCartItem.getQuantity() + cartRequest.getQuantity());
        exsistingCartItem.setPrice(singleItemPrice.multiply(BigDecimal.valueOf(exsistingCartItem.getQuantity())));
        cartItemRepository.save(exsistingCartItem);
    }

    private void addNewCartItem(String userid, String productId, CartRequest cartRequest) {
        CartItem cartItem = cartItemConverter.cartItemRequestToCartItemModel(cartRequest, userid, productId);
        cartItemRepository.save(cartItem);
    }
}
