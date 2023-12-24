package com.paypal.paypal.controller;

import com.paypal.paypal.dto.AuthRequest;
import com.paypal.paypal.dto.AuthResponse;
import com.paypal.paypal.model.CompletedOrder;
import com.paypal.paypal.model.PaymentOrder;
import com.paypal.paypal.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping(path = "/auth")
    public AuthResponse auth(@RequestBody AuthRequest authRequest) {
        return new AuthResponse(-1, "success paypal", 0.0);
    }

    @PostMapping(value = "/init")
    public PaymentOrder createPayment(
            @RequestParam("sum") Double sum) {
        return paymentService.createPayment(sum);
    } // endpoint koji ce da gadja PSP frontend u momentu - kupi (potrebno da salje amount)

    @PostMapping(value = "/capture")
    public CompletedOrder completePayment(@RequestParam("token") String token) {
        return paymentService.completePayment(token);
    }
}
