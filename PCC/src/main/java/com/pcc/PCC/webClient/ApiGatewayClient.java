package com.pcc.PCC.webClient;

import com.pcc.PCC.dto.PccRequest;
import com.pcc.PCC.dto.PccResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ApiGatewayClient {

    @PostMapping(path = "/redirecToSecondaryBank")
    public PccResponse redirecToSecondaryBank(@RequestBody PccRequest pccRequest);
}
