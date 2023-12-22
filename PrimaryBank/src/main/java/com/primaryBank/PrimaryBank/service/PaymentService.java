package com.primaryBank.PrimaryBank.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.primaryBank.PrimaryBank.dto.*;
import com.primaryBank.PrimaryBank.enums.PaymentStatus;
import com.primaryBank.PrimaryBank.model.Client;
import com.primaryBank.PrimaryBank.model.Transaction;
import com.primaryBank.PrimaryBank.repository.CardRepository;
import com.primaryBank.PrimaryBank.repository.ClientRepository;
import com.primaryBank.PrimaryBank.repository.TransactionRepository;
import com.primaryBank.PrimaryBank.webClient.ApiGatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.io.File;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private ApiGatewayClient apiGatewayClient;

    @Value("${bank.code}")
    private String bankCode;

    @Value("${bank.account}")
    private String bankAccountCode;

    public Integer clientExists(AuthRequest authRequest){
        Client client = clientRepository.findClientByMerchantId(authRequest.getMerchantId());
        if(client != null && client.getMerchantPassword().equals(authRequest.getMerchantPassword())){
            Transaction transaction = new Transaction(-1, authRequest.getMerchantOrderId(), authRequest.getMerchantId(),
                    authRequest.getAmount(), authRequest.getMerchantTimeStamp(), null, null, null);
            transaction = transactionRepository.save(transaction);
            return transaction.getPaymentId();
        }else {
            return null;
        }
    }

    public PaymentResponse checkIssuerBank(PaymentRequest paymentRequest) {
        try {
            Transaction transaction = transactionRepository.findTransactionByPaymentId(paymentRequest.getPaymentId());
            if(transaction.getMerchantTimeStamp().isBefore(LocalDateTime.now().minusMinutes(15))){
                return new PaymentResponse(transaction.getMerchantOrderId(), transaction.getPaymentId(),
                        transaction.getAcquiererTimestamp(), PaymentStatus.ERROR, null);
            }

            if(paymentRequest.getPan().substring(0, 6).equals(bankCode)) {
                //Transaction transaction = transactionRepository.findTransactionByPaymentId(paymentRequest.getPaymentId());

                //  this.executePayment(); //implementirati skidanje sa racuna kupca i dodavanje na racun prodavca
                ValidationDto creditIsValid = isCreditCardValid(paymentRequest);
                if (!creditIsValid.isValid()) {
                    transaction.setAcquiererTimestamp(LocalDateTime.now());
                    transaction.setIssuerTimestamp(LocalDateTime.now());
                    transaction.setPaymentStatus(PaymentStatus.FAILED);
                    transactionRepository.save(transaction);
                    return new PaymentResponse(transaction.getMerchantOrderId(), transaction.getPaymentId(),
                            transaction.getAcquiererTimestamp(), PaymentStatus.FAILED, null);
                }

                Transaction transaction1 = transactionRepository.findTransactionByPaymentId(paymentRequest.getPaymentId());
                if (transaction1.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
                    return new PaymentResponse(transaction.getMerchantOrderId(), transaction.getPaymentId(),
                            transaction1.getAcquiererTimestamp(), PaymentStatus.SUCCESS, creditIsValid.getIssuerTransactionId());
                } else {
                    return new PaymentResponse(transaction.getMerchantOrderId(), transaction.getPaymentId(),
                            transaction1.getAcquiererTimestamp(), PaymentStatus.ERROR, null);
                }

//            return new PaymentResponse();// skontati sta vratiti
            } else {
                //Transaction transaction = transactionRepository.findTransactionByPaymentId(paymentRequest.getPaymentId());
                transaction.setAcquiererTimestamp(LocalDateTime.now());
                transactionRepository.save(transaction);

                PccResponse response = apiGatewayClient.redirecToPcc(new PccRequest(paymentRequest.getPan(),
                        paymentRequest.getExpDate(), paymentRequest.getCvv(), paymentRequest.getCardHolderName(),
                        paymentRequest.getPaymentId(), transaction.getAcquiererTimestamp(), transaction.getAmount(), null));

                if(response.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
                    Client client = clientRepository.findClientByMerchantId(transaction.getMerchantId());
                    client.setAvailableSum(client.getAvailableSum() + transaction.getAmount());
                    clientRepository.save(client);

                    transaction.setPaymentStatus(PaymentStatus.SUCCESS);
                    transaction.setIssuerTimestamp(response.getIssuerTimestamp());
                    transactionRepository.save(transaction);

                    return new PaymentResponse(transaction.getMerchantOrderId(), response.getAcquirerOrderId(),
                            response.getAcquirerTimestamp(), PaymentStatus.SUCCESS, response.getIssuerOrderId());
                } else {
                    transaction.setIssuerTimestamp(response.getIssuerTimestamp());
                    transaction.setPaymentStatus(response.getPaymentStatus());
                    transactionRepository.save(transaction);
                    return new PaymentResponse(transaction.getMerchantOrderId(), response.getAcquirerOrderId(),
                            response.getAcquirerTimestamp(), response.getPaymentStatus(), response.getIssuerOrderId());
                }
            }
        } catch (NullPointerException e) {
            return new PaymentResponse(null, paymentRequest.getPaymentId(),
                    LocalDateTime.now(), PaymentStatus.ERROR, null);
        }
    }

    public ValidationDto isCreditCardValid(PaymentRequest paymentRequest) {
        Optional<Client> optionalClient = clientRepository.searchClientByPan(paymentRequest.getPan());
        Transaction transaction = transactionRepository.findTransactionByPaymentId(paymentRequest.getPaymentId());


        if (!optionalClient.isPresent()) {
            return new ValidationDto(false, null);
        }
            Client client = optionalClient.get();

        if (!paymentRequest.getCardHolderName().equals(client.getCardHolderName()) ||
                !paymentRequest.getPan().equals(client.getPan()) ||
                !paymentRequest.getCvv().equals(client.getCvv()) ||
                !isCreditCardDateValid(client.getExpDate())){

            return new ValidationDto(false, null);
        }

        if (client.getAvailableSum() < transaction.getAmount()) {
            return new ValidationDto(false, null);
        }

        double newSum = client.getAvailableSum() - transaction.getAmount();
        client.setAvailableSum(newSum);
        clientRepository.save(client);
        updateMerchantAccount(transaction);

        Transaction transaction1 = new Transaction(-1, transaction.getMerchantOrderId(), client.getMerchantId(),
                transaction.getAmount(), transaction.getMerchantTimeStamp(), PaymentStatus.SUCCESS, LocalDateTime.now(),
                LocalDateTime.now());

        Transaction issuerTransaction = transactionRepository.save(transaction1);


        return new ValidationDto(true, issuerTransaction.getPaymentId());
    }

    private void updateMerchantAccount(Transaction transaction) {
        Client client = clientRepository.findClientByMerchantId(transaction.getMerchantId());
        client.setAvailableSum(client.getAvailableSum() + transaction.getAmount());
        clientRepository.save(client);
        transaction.setPaymentStatus(PaymentStatus.SUCCESS);
        transaction.setAcquiererTimestamp(LocalDateTime.now());
        transaction.setIssuerTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    private boolean isCreditCardDateValid(String expDate) {
        String[] parts = expDate.split("/");
        YearMonth yearMonth = YearMonth.of(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
        int endDay = yearMonth.lengthOfMonth();
        LocalDateTime expDateTime = LocalDateTime.of(2000 + Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), endDay,
                23, 59, 59);
        return expDateTime.isAfter(LocalDateTime.now());
    }

    public PccResponse issuerBankPayment(PccRequest pccRequest) {
        try {
            Client client = clientRepository.findClientByPan(pccRequest.getPan());

            String[] parts = client.getExpDate().split("/");
            YearMonth yearMonth = YearMonth.of(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
            int endDay = yearMonth.lengthOfMonth();
            LocalDateTime expDate = LocalDateTime.of(2000 + Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), endDay,
                    23, 59, 59);

            if(client != null && client.getExpDate().equals(pccRequest.getExpDate()) && client.getCvv().equals(pccRequest.getCvv())
                    && expDate.isAfter(LocalDateTime.now()) && client.getAvailableSum() >= pccRequest.getAmount()) {

                double newSum = client.getAvailableSum() - pccRequest.getAmount();
                client.setAvailableSum(newSum);
                Transaction transaction = new Transaction(-1, (long) -1, client.getMerchantId(), pccRequest.getAmount(),
                        null, PaymentStatus.SUCCESS, pccRequest.getAcquiererTimestamp(), LocalDateTime.now()); // sta sa mrcent order id
                Transaction newTransaction = transactionRepository.save(transaction);
                PccResponse pccResponse = new PccResponse(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(),
                        newTransaction.getPaymentId(), transaction.getIssuerTimestamp(), PaymentStatus.SUCCESS);
                return pccResponse;
            } else {
                //da li da kreiram failed transakciju
                return new PccResponse(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(),
                        -1, LocalDateTime.now(), PaymentStatus.FAILED);
            }
        } catch (NullPointerException e) {
            return new PccResponse(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(),
                    -1, LocalDateTime.now(), PaymentStatus.ERROR);
        }
    }

    public List<Transaction> getAll(){
        return transactionRepository.findAll();
    }

    public AuthResponse generateQRcode(AuthRequest authRequest) throws WriterException, IOException {
        Client client = clientRepository.findClientByMerchantId(authRequest.getMerchantId());
        if(client != null && client.getMerchantPassword().equals(authRequest.getMerchantPassword())){
            Transaction transaction = new Transaction(-1, authRequest.getMerchantOrderId(), authRequest.getMerchantId(),
                    authRequest.getAmount(), authRequest.getMerchantTimeStamp(), null, null, null);
            transaction = transactionRepository.save(transaction);

            int width = 300;
            int height = 300;

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode("Amount:"+transaction.getAmount()+"\n"+
                    "Currency:"+"RSD"+"\n"+
                    "Name:"+client.getName()+"\n"+
                    "Account number:"+client.getAccountNumber(), BarcodeFormat.QR_CODE, width, height, hints);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            String folderPath = "E:\\SEP projekat\\SEP\\PSPFrontend\\src\\assets";

            String fileName = transaction.getPaymentId()+ "qrCodeImage.png";

            String filePath = folderPath + File.separator + fileName;

            ImageIO.write(bufferedImage, "png", new File(filePath));

            AuthResponse response = new AuthResponse(transaction.getPaymentId(), "success",
                    transaction.getAmount(), fileName);

            return response;
        }else {
            return null;
        }
    }

    public PaymentResponse payQrCode(PaymentRequest paymentRequest) {

        Transaction transaction = transactionRepository.findTransactionByPaymentId(paymentRequest.getPaymentId());
        if(transaction.getMerchantTimeStamp().isBefore(LocalDateTime.now().minusMinutes(15))){
            return new PaymentResponse(transaction.getMerchantOrderId(), transaction.getPaymentId(),
                    transaction.getAcquiererTimestamp(), PaymentStatus.ERROR, null);
        }

        if(paymentRequest.getAccountNumber().substring(0, 3).equals(bankAccountCode)){

            ValidationDto hasEnoughMoney = hasEnoughMoney(paymentRequest, transaction);

            if (!hasEnoughMoney.isValid()) {
                transaction.setAcquiererTimestamp(LocalDateTime.now());
                transaction.setIssuerTimestamp(LocalDateTime.now());
                transaction.setPaymentStatus(PaymentStatus.FAILED);
                transactionRepository.save(transaction);
                return new PaymentResponse(transaction.getMerchantOrderId(), transaction.getPaymentId(),
                        transaction.getAcquiererTimestamp(), PaymentStatus.FAILED, null);
            }

            Transaction transactionAfterUpdate = transactionRepository.findTransactionByPaymentId(paymentRequest.getPaymentId());
            if (transactionAfterUpdate.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
                return new PaymentResponse(transaction.getMerchantOrderId(), transaction.getPaymentId(),
                        transactionAfterUpdate.getAcquiererTimestamp(), PaymentStatus.SUCCESS, hasEnoughMoney.getIssuerTransactionId());
            } else {
                return new PaymentResponse(transaction.getMerchantOrderId(), transaction.getPaymentId(),
                        transactionAfterUpdate.getAcquiererTimestamp(), PaymentStatus.ERROR, null);
            }

        } else {

            transaction.setAcquiererTimestamp(LocalDateTime.now());
            transactionRepository.save(transaction);

            PccResponse response = apiGatewayClient.redirecToPccQRcode(new PccRequest(paymentRequest.getPan(),
                    paymentRequest.getExpDate(), paymentRequest.getCvv(), paymentRequest.getCardHolderName(),
                    paymentRequest.getPaymentId(), transaction.getAcquiererTimestamp(), transaction.getAmount(),
                    paymentRequest.getAccountNumber()));

            if(response.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
                Client client = clientRepository.findClientByMerchantId(transaction.getMerchantId());
                client.setAvailableSum(client.getAvailableSum() + transaction.getAmount());
                clientRepository.save(client);

                transaction.setPaymentStatus(PaymentStatus.SUCCESS);
                transaction.setIssuerTimestamp(response.getIssuerTimestamp());
                transactionRepository.save(transaction);

                return new PaymentResponse(transaction.getMerchantOrderId(), response.getAcquirerOrderId(),
                        response.getAcquirerTimestamp(), PaymentStatus.SUCCESS, response.getIssuerOrderId());
            } else {
                transaction.setIssuerTimestamp(response.getIssuerTimestamp());
                transaction.setPaymentStatus(response.getPaymentStatus());
                transactionRepository.save(transaction);
                return new PaymentResponse(transaction.getMerchantOrderId(), response.getAcquirerOrderId(),
                        response.getAcquirerTimestamp(), response.getPaymentStatus(), response.getIssuerOrderId());
            }
        }
    }

    private ValidationDto hasEnoughMoney(PaymentRequest paymentRequest, Transaction transaction) {
        Optional<Client> optionalClient = clientRepository.searchClientByAccountNumber(paymentRequest.getAccountNumber());

        if (!optionalClient.isPresent()) {
            return new ValidationDto(false, null);
        }
        Client client = optionalClient.get();

        if (client.getAvailableSum() < transaction.getAmount()){
            return new ValidationDto(false, null);
        }

        double newSum = client.getAvailableSum() - transaction.getAmount();
        client.setAvailableSum(newSum);
        clientRepository.save(client);
        updateMerchantAccount(transaction);

        Transaction transaction1 = new Transaction(-1, transaction.getMerchantOrderId(), client.getMerchantId(),
                transaction.getAmount(), transaction.getMerchantTimeStamp(), PaymentStatus.SUCCESS, LocalDateTime.now(),
                LocalDateTime.now());

        Transaction issuerTransaction = transactionRepository.save(transaction1);


        return new ValidationDto(true, issuerTransaction.getPaymentId());
    }

    public PccResponse issuerBankPaymentQRcode(PccRequest pccRequest) {
        try {
            Optional<Client> optionalClient = clientRepository.searchClientByAccountNumber(pccRequest.getAccountNumber());
            if(optionalClient.isPresent() && optionalClient.get().getAvailableSum()>=pccRequest.getAmount()){
                Client client = optionalClient.get();
                clientRepository.save(client);

                double newSum = client.getAvailableSum() - pccRequest.getAmount();
                client.setAvailableSum(newSum);
                Transaction transaction = new Transaction(-1, (long) -1, client.getMerchantId(), pccRequest.getAmount(),
                        null, PaymentStatus.SUCCESS, pccRequest.getAcquiererTimestamp(), LocalDateTime.now());
                Transaction newTransaction = transactionRepository.save(transaction);
                PccResponse pccResponse = new PccResponse(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(),
                        newTransaction.getPaymentId(), transaction.getIssuerTimestamp(), PaymentStatus.SUCCESS);
                return pccResponse;
            } else {
                return new PccResponse(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(),
                        -1, LocalDateTime.now(), PaymentStatus.FAILED);
            }

        } catch (NullPointerException e) {
            return new PccResponse(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(),
                    -1, LocalDateTime.now(), PaymentStatus.ERROR);
        }
    }

}
