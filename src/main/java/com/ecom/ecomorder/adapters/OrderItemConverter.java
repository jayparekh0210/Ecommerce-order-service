package com.ecom.ecomorder.adapters;
import com.ecom.ecomorder.dto.internal.responses.OrderItemDTO;
import com.ecom.ecomorder.dto.internal.responses.OrderResponse;
import com.ecom.ecomorder.models.CartItem;
import com.ecom.ecomorder.models.Order;
import com.ecom.ecomorder.models.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OrderItemConverter {
    public OrderItem cartItemToOrderItem(CartItem cartItem, Order order) {
        return OrderItem.builder()
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .order(order)
                .build();
    }

    public OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .subtotal(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .build();
    }

    public OrderResponse orderModelToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderItems(order.getOrderItems().stream()
                        .map(this::orderItemToOrderItemDTO)
                        .toList())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
