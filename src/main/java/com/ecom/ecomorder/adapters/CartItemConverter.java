package com.ecom.ecomorder.adapters;


import com.ecom.ecomorder.dto.requests.CartRequest;
import com.ecom.ecomorder.dto.responses.CartResponse;
import com.ecom.ecomorder.models.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CartItemConverter {

    public CartItem cartItemRequestToCartItemModel(CartRequest cartRequest, String userId,String productId) {
        return CartItem.builder()
                .productId(productId)
                .userId(userId)
                .quantity(cartRequest.getQuantity())
                //Setting Hard coded value as of now will fix once we do inter-service communication
                .price(BigDecimal.valueOf(100.00).multiply(BigDecimal.valueOf(cartRequest.getQuantity())))
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
