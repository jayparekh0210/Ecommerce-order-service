package com.ecom.ecomorder.dto.internal.requests;

import lombok.Data;

@Data
public class CartRequest {
    private String productId;
    private Integer quantity;
}
