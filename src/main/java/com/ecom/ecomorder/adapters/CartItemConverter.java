package com.ecom.ecomorder.adapters;


import com.ecom.ecomorder.dto.external.responses.ProductResponseDTO;
import com.ecom.ecomorder.dto.internal.requests.CartRequest;
import com.ecom.ecomorder.dto.internal.responses.CartResponse;
import com.ecom.ecomorder.models.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CartItemConverter {

    public CartItem cartItemRequestToCartItemModel(CartRequest cartRequest, String userId, ProductResponseDTO product) {
        return CartItem.builder()
                .productId(product.getId())
                .userId(userId)
                .quantity(cartRequest.getQuantity())
                .price(product.getPrice().multiply(BigDecimal.valueOf(cartRequest.getQuantity())))
                .build();
    }

    public CartResponse cartItemModelToCartResponse(CartItem cartItem) {
        return CartResponse.builder()
                .productId(cartItem.getProductId())
                .userId(cartItem.getUserId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .build();
    }


}
