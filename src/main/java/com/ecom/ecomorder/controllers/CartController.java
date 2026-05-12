package com.ecom.ecomorder.controllers;


import com.ecom.ecomorder.dto.internal.requests.CartRequest;
import com.ecom.ecomorder.dto.internal.responses.CartResponse;
import com.ecom.ecomorder.services.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CartRequest cartRequest
    ) {
        if (cartItemService.addToCart(userId, cartRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product out of stock or invalid product ID or user not found");

    }

    @DeleteMapping("/removeFromCart/{productId}")
    public ResponseEntity<String> removeFromCart(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String productId
    ) {
        if(cartItemService.removeFromCart(userId, productId)){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found in cart or user not found");
    }

    @DeleteMapping("/deleteCartItem/{productId}")
    public ResponseEntity<String> deleteCartItem(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String productId
    ) {
        if(cartItemService.deleteCartItem(userId, productId)){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found in cart or user not found");

    }

    @GetMapping("/getCartItems")
    public ResponseEntity<List<CartResponse>> getCartItems(@RequestHeader("X-User-Id") String userId){
        List<CartResponse> cartItems = cartItemService.getCartItems(userId);
        if(cartItems != null){
            return ResponseEntity.status(HttpStatus.OK).body(cartItems);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
