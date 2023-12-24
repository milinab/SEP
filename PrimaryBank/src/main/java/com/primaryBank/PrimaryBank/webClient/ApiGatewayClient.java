package com.primaryBank.PrimaryBank.webClient;

import com.primaryBank.PrimaryBank.dto.PccRequest;
import com.primaryBank.PrimaryBank.dto.PccResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface ApiGatewayClient {

    @PostExchange("/api/proxy/redirecToPcc")
    public PccResponse redirecToPcc(@RequestBody PccRequest pccRequest);

    @PostExchange("/api/proxy/redirecToPccQRcode")
    public PccResponse redirecToPccQRcode(@RequestBody PccRequest pccRequest);
}
