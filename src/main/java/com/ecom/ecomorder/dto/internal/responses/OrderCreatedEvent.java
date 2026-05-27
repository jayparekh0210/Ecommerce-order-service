package com.ecom.ecomorder.dto.internal.responses;

import com.ecom.ecomorder.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long id;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String userId;
    private List<OrderItemDTO> orderItems;
    private LocalDateTime createdAt;
}
