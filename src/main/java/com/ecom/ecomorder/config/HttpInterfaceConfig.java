package com.ecom.ecomorder.config;

import com.ecom.ecomorder.api.clients.HttpInterfaceProductService;
import com.ecom.ecomorder.api.clients.HttpInterfaceUserService;
import com.ecom.ecomorder.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Bean
    public HttpInterfaceProductService httpInterfaceService(
            @Qualifier("loadBalancedRestClientBuilder")
            RestClient.Builder restClientBuilder
    ) {
        RestClient restClient = restClientBuilder
                .baseUrl("http://productService/api/product")
                .build();
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(restClient))
                .build();

        return proxyFactory.createClient(HttpInterfaceProductService.class);

    }

    @Bean
    public HttpInterfaceUserService httpInterfaceUserService(
            @Qualifier("loadBalancedRestClientBuilder")
            RestClient.Builder restClientBuilder
    ) {
        RestClient restClient = restClientBuilder
                .baseUrl("http://userService/api/user")
                .defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        (request, response) -> {

                            if(response.getStatusCode().value() == 404) {

                                throw new UserNotFoundException(
                                        "User not found"
                                );
                            }

                            throw new RuntimeException(
                                    "Client error occurred"
                            );
                        }
                )
                .build();
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(restClient))
                .build();

        return proxyFactory.createClient(HttpInterfaceUserService.class);
    }

}
