package com.ecom.ecomorder.validations;
import com.ecom.ecomorder.dto.external.responses.ProductResponseDTO;
import com.ecom.ecomorder.dto.external.responses.UserResponse;
import com.ecom.ecomorder.dto.internal.requests.CartRequest;
import com.ecom.ecomorder.models.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartValidation {
    public boolean isAddToCartValid(UserResponse user, ProductResponseDTO product, CartRequest cartRequest) {
        return user != null && product != null && product.getStockQuantity() >= cartRequest.getQuantity() && product.getIsActive();
    }

    public boolean AddToExistingCartValid(ProductResponseDTO product, CartItem existingCartItem, CartRequest cartRequest) {
        return product != null && product.getStockQuantity() >= cartRequest.getQuantity() + existingCartItem.getQuantity() && product.getIsActive();
    }

    public boolean isRemoveFromCartValid(UserResponse user, ProductResponseDTO product) {
        return user != null && product != null;
    }
}

