package com.ecom.ecomorder.api.clients;

import com.ecom.ecomorder.dto.external.responses.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface HttpInterfaceUserService {

    @GetExchange("/get/{id}")
    UserResponse getUserById(@PathVariable String id);


}
