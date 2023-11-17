package com.pcc.PCC.webClient;

import com.pcc.PCC.dto.PccRequest;
import com.pcc.PCC.dto.PccResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface PrimaryBankClient {

    @PostExchange("/api/payment/issuerPayment")
    public PccResponse issuerBankPayment(@RequestBody PccRequest authRequest);
}
