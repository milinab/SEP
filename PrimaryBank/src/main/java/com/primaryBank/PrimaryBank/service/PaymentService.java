package com.primaryBank.PrimaryBank.service;

import com.primaryBank.PrimaryBank.dto.AuthRequest;
import com.primaryBank.PrimaryBank.dto.PaymentRequest;
import com.primaryBank.PrimaryBank.dto.PaymentResponse;
import com.primaryBank.PrimaryBank.dto.PccRequest;
import com.primaryBank.PrimaryBank.dto.PccResponse;
import com.primaryBank.PrimaryBank.enums.PaymentStatus;
import com.primaryBank.PrimaryBank.model.Card;
import com.primaryBank.PrimaryBank.model.Client;
import com.primaryBank.PrimaryBank.model.Transaction;
import com.primaryBank.PrimaryBank.repository.CardRepository;
import com.primaryBank.PrimaryBank.repository.ClientRepository;
import com.primaryBank.PrimaryBank.repository.TransactionRepository;
import com.primaryBank.PrimaryBank.webClient.PccClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
public class PaymentService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PccClient pccClient;

    @Value("${bank.code}")
    private String bankCode;

    public Integer clientExists(AuthRequest authRequest){
        Client client = clientRepository.findClientByMerchantId(authRequest.getMerchantId());
        if(client != null && client.getMerchantPassword().equals(authRequest.getMerchantPassword())){
            Transaction transaction = new Transaction(-1, authRequest.getMerchantOrderId(), authRequest.getMerchantId(),
                    authRequest.getAmount(), authRequest.getMerchantTimeStamp(), PaymentStatus.FAILED);
            transaction = transactionRepository.save(transaction);
            return transaction.getPaymentId();
        }else {
            return null;
        }
    }

    public PaymentResponse checkIssuerBank(PaymentRequest paymentRequest) {
        if(paymentRequest.getPan().substring(4, 8).equals(bankCode)) {
            this.executePayment(); //implementirati skidanje sa racuna kupca i dodavanje na racun prodavca
            return new PaymentResponse();// skontati sta vratiti
        } else {
            Transaction transaction = transactionRepository.findTransactionByPaymentId(paymentRequest.getPaymentId());
            PccResponse response = pccClient.sendToIssuerBank(new PccRequest(paymentRequest.getPan(),
                    paymentRequest.getExpDate(), paymentRequest.getCvv(), paymentRequest.getCardHolderName(),
                    paymentRequest.getPaymentId(), LocalDateTime.now(), transaction.getAmount()));
            System.out.println("pcc response");
            System.out.println(response.getAcquirerOrderId());
            System.out.println(response.getPaymentStatus());
            if(response.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
                Client client = clientRepository.findClientByMerchantId(transaction.getMerchantId());
                client.setAvailableSum(client.getAvailableSum() + transaction.getAmount());
                clientRepository.save(client);

                transaction.setPaymentStatus(PaymentStatus.SUCCESS);
                transactionRepository.save(transaction);

                return new PaymentResponse(transaction.getMerchantOrderId(), response.getAcquirerOrderId(),
                        response.getAcquirerTimestamp(), PaymentStatus.SUCCESS);
            } else {
                return new PaymentResponse(transaction.getMerchantOrderId(), response.getAcquirerOrderId(),
                        response.getAcquirerTimestamp(), response.getPaymentStatus());
            }
        }
    }

    private void executePayment(){}

    public PccResponse issuerBankPayment(PccRequest pccRequest) {
        Client client = clientRepository.findClientByPan(pccRequest.getPan());

        String[] parts = client.getExpDate().split("/");
        YearMonth yearMonth = YearMonth.of(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
        int endDay = yearMonth.lengthOfMonth();
        LocalDateTime expDate = LocalDateTime.of(2000 + Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), endDay,
                23, 59, 59);

        if(client != null && client.getExpDate().equals(pccRequest.getExpDate()) && client.getCvv().equals(pccRequest.getCvv())
                && expDate.isAfter(LocalDateTime.now()) && client.getAvailableSum() >= pccRequest.getAmount()){
            double newSum = client.getAvailableSum() - pccRequest.getAmount();
            client.setAvailableSum(newSum);
            Transaction transaction = new Transaction(-1, -1, client.getMerchantId(), pccRequest.getAmount(),
                    null, PaymentStatus.SUCCESS); // sta sa mrcent order id
            Transaction newTransaction = transactionRepository.save(transaction);
            PccResponse pccResponse = new PccResponse(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(),
                    newTransaction.getPaymentId(), LocalDateTime.now(), PaymentStatus.SUCCESS);
            return pccResponse;
        } else {
            return new PccResponse(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(),
                    -1, LocalDateTime.now(), PaymentStatus.FAILED);
        }
    }
}
