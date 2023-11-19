package com.secondaryBank.SecondaryBank;

import com.secondaryBank.SecondaryBank.dto.PccRequest;
import com.secondaryBank.SecondaryBank.dto.PccResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    @PostMapping("/issuerPayment")
    public PccResponse issuerBankPayment(@RequestBody PccRequest authRequest){
        return new PccResponse();
    }
}
