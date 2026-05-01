package com.ecom.ecomorder.services;

import com.ecom.ecomorder.adapters.OrderItemConverter;
import com.ecom.ecomorder.dto.responses.OrderResponse;
import com.ecom.ecomorder.models.OrderStatus;
import com.ecom.ecomorder.models.CartItem;
import com.ecom.ecomorder.models.Order;
import com.ecom.ecomorder.models.OrderItem;

import com.ecom.ecomorder.repositories.CartItemRepository;
import com.ecom.ecomorder.repositories.OrderRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Data
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemConverter orderItemConverter;

    public OrderResponse createOrder(Long userId) {
        if (userId != null) {
            List<CartItem> cartItems = cartItemRepository.findCartItemsByUserId(String.valueOf(userId));
            if (!cartItems.isEmpty()) {
                Order savedOrder = saveOrderToDb(cartItems, String.valueOf(userId));
                cartItemRepository.deleteAll(cartItems);
                return orderItemConverter.orderModelToOrderResponse(savedOrder);
            } else {
                return null;
            }
        }
        return null;

    }

    private Order saveOrderToDb(List<CartItem> cartItems, String userId) {
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = Order.builder()
                .userId(userId)
                .status(OrderStatus.CONFIRMED)
                .totalAmount(totalPrice)
                .build();
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> orderItemConverter.cartItemToOrderItem(cartItem, order))
                .toList();
        order.setOrderItems(orderItems);
        return orderRepository.save(order);

    }

}
