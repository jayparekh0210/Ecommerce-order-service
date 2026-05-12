package com.ecom.ecomorder.services;

import com.ecom.ecomorder.adapters.CartItemConverter;
import com.ecom.ecomorder.api.clients.HttpInterfaceProductService;
import com.ecom.ecomorder.api.clients.HttpInterfaceUserService;
import com.ecom.ecomorder.dto.external.responses.ProductResponseDTO;
import com.ecom.ecomorder.dto.external.responses.UserResponse;
import com.ecom.ecomorder.dto.internal.requests.CartRequest;
import com.ecom.ecomorder.dto.internal.responses.CartResponse;
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
    private final HttpInterfaceProductService httpInterfaceProductService;
    private final HttpInterfaceUserService httpInterfaceUserService;

    public boolean addToCart(String userId, CartRequest cartRequest) {
        ProductResponseDTO productResponse = httpInterfaceProductService.getProductById(cartRequest.getProductId());
        UserResponse userResponse = httpInterfaceUserService.getUserById(userId);

        if (cartValidation.isAddToCartValid(userResponse, productResponse, cartRequest)) {
            CartItem exsistingCartItem = cartItemRepository.findByUserIdAndProductId(userId, productResponse.getId());
            if (exsistingCartItem != null) {
                if (cartValidation.AddToExistingCartValid(productResponse,exsistingCartItem,cartRequest)) {
                    addMoreQuantityToCart(exsistingCartItem, cartRequest,productResponse);
                } else {
                    return false;
                }
            } else {
                addNewCartItem(userId, productResponse, cartRequest);
            }
            return true;
        }
        return false;
    }

    public boolean deleteCartItem(String userId, String productId) {

        ProductResponseDTO productResponse = httpInterfaceProductService.getProductById(productId);
        UserResponse userResponse = httpInterfaceUserService.getUserById(userId);

        if (cartValidation.isRemoveFromCartValid(userResponse, productResponse)) {
            CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
            if (cartItem != null) {
                cartItemRepository.delete(cartItem);
                return true;
            }
        }
        return false;
    }

    public boolean removeFromCart(String userId, String productId) {
        ProductResponseDTO productResponse = httpInterfaceProductService.getProductById(productId);
        UserResponse userResponse = httpInterfaceUserService.getUserById(userId);

        if (cartValidation.isRemoveFromCartValid(userResponse,productResponse)) {
            CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, String.valueOf(productId));
            if (cartItem != null) {
                if (cartItem.getQuantity() > 1) {
                    decreaseQuantityFromCart(cartItem, productResponse);
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

    private void decreaseQuantityFromCart(CartItem cartItem, ProductResponseDTO product) {
        cartItem.setQuantity(cartItem.getQuantity() - 1);
        cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        cartItemRepository.save(cartItem);
    }

    private void addMoreQuantityToCart(CartItem exsistingCartItem, CartRequest cartRequest, ProductResponseDTO product) {
        exsistingCartItem.setQuantity(exsistingCartItem.getQuantity() + cartRequest.getQuantity());
        exsistingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(exsistingCartItem.getQuantity())));
        cartItemRepository.save(exsistingCartItem);
    }

    private void addNewCartItem(String userid, ProductResponseDTO product, CartRequest cartRequest) {
        CartItem cartItem = cartItemConverter.cartItemRequestToCartItemModel(cartRequest, userid, product);
        cartItemRepository.save(cartItem);
    }
}
