package com.ecom.ecomorder.services;

import com.ecom.ecomorder.adapters.OrderItemConverter;
import com.ecom.ecomorder.dto.internal.responses.OrderCreatedEvent;
import com.ecom.ecomorder.dto.internal.responses.OrderResponse;
import com.ecom.ecomorder.models.OrderStatus;
import com.ecom.ecomorder.models.CartItem;
import com.ecom.ecomorder.models.Order;
import com.ecom.ecomorder.models.OrderItem;

import com.ecom.ecomorder.repositories.CartItemRepository;
import com.ecom.ecomorder.repositories.OrderRepository;
import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import static com.ecom.ecomorder.constants.RabbitMQConstant.*;

import java.math.BigDecimal;
import java.util.List;

@Service
@Data
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemConverter orderItemConverter;
    private final StreamBridge streamBridge;

    public OrderResponse createOrder(String userId) {
        if (userId != null) {
            List<CartItem> cartItems = cartItemRepository.findCartItemsByUserId(userId);
            if (!cartItems.isEmpty()) {
                Order savedOrder = saveOrderToDb(cartItems,userId);
                cartItemRepository.deleteAll(cartItems);
                OrderCreatedEvent orderCreatedEvent = orderItemConverter.orederToOrderCreatedEvent(savedOrder);
                streamBridge.send(ORDER_STREAM_BUILDING_QUEUE_NAME, orderCreatedEvent);
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
