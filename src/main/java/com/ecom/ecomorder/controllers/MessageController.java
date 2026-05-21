package com.ecom.ecomorder.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/message")
public class MessageController {
    @Value("${app.message}")
    private String message;

    @RateLimiter(name = "rateKiller", fallbackMethod = "getMessageFallback")
    @GetMapping
    public String getMessage(){
        return message;
    }

    public String getMessageFallback(Exception ex){
        return "Fallback message: Service is currently unavailable. Please try again later.";
    }
}
