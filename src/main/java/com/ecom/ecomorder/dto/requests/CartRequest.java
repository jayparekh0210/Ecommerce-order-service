package com.ecom.ecomorder.dto.requests;

import lombok.Data;

@Data
public class CartRequest {
    private Long productId;
    private Integer quantity;
}
