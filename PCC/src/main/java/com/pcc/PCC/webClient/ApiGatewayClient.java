package com.pcc.PCC.webClient;

import com.pcc.PCC.dto.PccRequest;
import com.pcc.PCC.dto.PccResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface ApiGatewayClient {

    @PostExchange("/api/proxy/redirecToSecondaryBank")
    public PccResponse redirecToSecondaryBank(@RequestBody PccRequest pccRequest);

    @PostExchange("/api/proxy/redirecToSecondaryBankQRcode")
    public PccResponse redirecToSecondaryBankQRcode(@RequestBody PccRequest pccRequest);
}
