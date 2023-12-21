package com.psp.PSPBackend.webClient;

import com.psp.PSPBackend.dto.AuthRequest;
import com.psp.PSPBackend.dto.AuthResponse;
import com.psp.PSPBackend.dto.TransactionDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange
public interface PrimaryBankClient {

    @PostExchange("/api/payment/auth")
    public AuthResponse auth(@RequestBody AuthRequest authRequest);

    @PostExchange("/api/payment/generateQRcode")
    public AuthResponse generateQRcode(@RequestBody AuthRequest authRequest);

    @GetExchange("/api/payment/getTransactions")
    public List<TransactionDto> getTransactions();
}
