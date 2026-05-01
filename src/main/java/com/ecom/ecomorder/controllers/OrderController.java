package com.ecom.ecomorder.controllers;


import com.ecom.ecomorder.dto.responses.OrderResponse;
import com.ecom.ecomorder.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @RequestMapping("/create")
    public ResponseEntity<?> createOrder(
            @RequestHeader("X-User-Id") String userId
    ) {
        OrderResponse orderResponse = orderService.createOrder(Long.parseLong(userId));
        if(orderResponse == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found or cart is empty");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
