package com.ecom.ecomorder.repositories;


import com.ecom.ecomorder.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserIdAndProductId(String userId, String productId) ;

    List<CartItem> findCartItemsByUserId(String userId);

}
