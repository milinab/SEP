package com.paypal.paypal.controller;

import com.paypal.paypal.dto.AuthRequest;
import com.paypal.paypal.dto.AuthResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    @PostMapping(path = "/auth")
    public AuthResponse auth(@RequestBody AuthRequest authRequest) {
        return new AuthResponse(-1, "success paypal", 0.0);
    }
}
