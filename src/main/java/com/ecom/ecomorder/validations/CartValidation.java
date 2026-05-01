package com.ecom.ecomorder.validations;
import org.springframework.stereotype.Component;

@Component
public class CartValidation {
    public boolean isAddToCartValid(String userId, String productId) {
        return userId != null && productId != null;
    }

    public boolean AddToExistingCartValid(String productId) {
        return productId != null;
    }

    public boolean isRemoveFromCartValid(String userId, String productId) {
        return userId != null && productId != null;
    }
}

