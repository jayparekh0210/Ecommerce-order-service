package com.ecom.ecomorder.api.clients;

import com.ecom.ecomorder.dto.external.responses.ProductResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface HttpInterfaceProductService {

    @GetExchange("/get/{id}")
    ProductResponseDTO getProductById(@PathVariable String id);

}
