package com.ecom.ecomorder.validations;
import com.ecom.ecomorder.dto.external.responses.ProductResponseDTO;
import com.ecom.ecomorder.dto.external.responses.UserResponse;
import com.ecom.ecomorder.dto.internal.requests.CartRequest;
import com.ecom.ecomorder.exceptions.ProductNotFoundException;
import com.ecom.ecomorder.exceptions.UserNotFoundException;
import com.ecom.ecomorder.models.CartItem;
import org.springframework.stereotype.Component;

import static com.ecom.ecomorder.constants.ErrorTextConstant.*;

@Component
public class CartValidation {
    public boolean isAddToCartValid(UserResponse user, ProductResponseDTO product, CartRequest cartRequest) {
        if(user == null){
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }

        if(!(product.getStockQuantity() >= cartRequest.getQuantity())){
            throw new ProductNotFoundException(PRODUCT_OUT_OF_STOCK);
        }

        if(!(product.getIsActive())){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        return  true;
    }

    public boolean AddToExistingCartValid(ProductResponseDTO product, CartItem existingCartItem, CartRequest cartRequest) {
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }

        if(!(product.getStockQuantity() >= cartRequest.getQuantity() + existingCartItem.getQuantity() )){
            throw new ProductNotFoundException(PRODUCT_OUT_OF_STOCK);
        }

        if(!(product.getIsActive())){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }

        return  true;
    }

    public boolean isRemoveFromCartValid(UserResponse user, ProductResponseDTO product) {
        if(user == null){
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        if(product == null){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        return  true;
    }
}

