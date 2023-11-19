package com.psp.PSPBackend.webClient;

import com.psp.PSPBackend.dto.AuthRequest;
import com.psp.PSPBackend.dto.AuthResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface CryptoClient {
    @PostExchange("/api/payment/auth")
    public AuthResponse auth(@RequestBody AuthRequest authRequest);
}
