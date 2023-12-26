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

import javax.crypto.SecretKey;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private KeyStoreService keyStoreService;

    @Value("${bank.code}")
    private String bankCode;

    @Value("${bank.account}")
    private String bankAccountCode;

    public Integer clientExists(AuthRequest authRequest) throws Exception {
        Client client = clientRepository.findClientByMerchantId(authRequest.getMerchantId());

        SecretKey key = keyStoreService.getKey(client.getMerchantId(), client.getPan());
        client.setMerchantPassword(keyStoreService.decrypt(client.getMerchantPassword(), key));

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
            } else {

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
        } catch (Exception e) {
            return new PaymentResponse(null, paymentRequest.getPaymentId(),
                    LocalDateTime.now(), PaymentStatus.ERROR, null);
        }
    }

    public ValidationDto isCreditCardValid(PaymentRequest paymentRequest) throws Exception {
        Optional<Client> optionalClient = clientRepository.searchClientByPan(paymentRequest.getPan());
        Transaction transaction = transactionRepository.findTransactionByPaymentId(paymentRequest.getPaymentId());


        if (!optionalClient.isPresent()) {
            return new ValidationDto(false, null);
        }
            Client client = optionalClient.get();

            SecretKey key = keyStoreService.getKey(client.getMerchantId(), client.getPan());

        if (!paymentRequest.getCardHolderName().equals(keyStoreService.decrypt(client.getCardHolderName(), key)) ||
                !paymentRequest.getPan().equals(client.getPan()) ||
                !paymentRequest.getCvv().equals(keyStoreService.decrypt(client.getCvv(), key)) ||
                !isCreditCardDateValid(keyStoreService.decrypt(client.getExpDate(), key))){

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
            SecretKey key = keyStoreService.getKey(client.getMerchantId(), client.getPan());

            String[] parts = keyStoreService.decrypt(client.getExpDate(), key).split("/");
            YearMonth yearMonth = YearMonth.of(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
            int endDay = yearMonth.lengthOfMonth();
            LocalDateTime expDate = LocalDateTime.of(2000 + Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), endDay,
                    23, 59, 59);

            if(client != null && keyStoreService.decrypt(client.getExpDate(), key).equals(pccRequest.getExpDate()) && keyStoreService.decrypt(client.getCvv(), key).equals(pccRequest.getCvv())
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
        } catch (Exception e) {
            return new PccResponse(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(),
                    -1, LocalDateTime.now(), PaymentStatus.ERROR);
        }
    }

    public List<Transaction> getAll(){
        return transactionRepository.findAll();
    }

    public AuthResponse generateQRcode(AuthRequest authRequest) throws Exception {

        Client client = clientRepository.findClientByMerchantId(authRequest.getMerchantId());
        SecretKey key = keyStoreService.getKey(client.getMerchantId(), client.getPan());
        client.setMerchantPassword(keyStoreService.decrypt(client.getMerchantPassword(), key));
        client.setName(keyStoreService.decrypt(client.getName(), key));

        if(client != null && client.getMerchantPassword().equals(authRequest.getMerchantPassword())){
            Transaction transaction = new Transaction(-1, authRequest.getMerchantOrderId(), authRequest.getMerchantId(),
                    authRequest.getAmount(), authRequest.getMerchantTimeStamp(), null, null, null);
            transaction = transactionRepository.save(transaction);

            int width = 300;
            int height = 300;

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String content = "K:PR|V:01|C:1|R:"+client.getAccountNumber()+"|"+
                    "N:"+client.getName()+"|"+
                    "I:RSD"+transaction.getAmount()+"|"+
                    "SF:221";

            if(!validateQRcode(content)){
                return null;
            }

            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);



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

    private boolean validateQRcode(String qrCode){

        String[] parts = qrCode.split("\\|");

        String regexAccountNumber = "[0-9]{18}$";
        Pattern patternAccountNumber = Pattern.compile(regexAccountNumber);
        String accountNumber = parts[3].split(":")[1];
        Matcher matcherAccountNumber = patternAccountNumber.matcher(accountNumber);
        boolean an = matcherAccountNumber.matches();


        String regexName = "[A-Za-z ]{3,20}$";
        Pattern patternName = Pattern.compile(regexName);
        Matcher matcherName = patternName.matcher(parts[4].split(":")[1]);
        boolean n = matcherName.matches();

        String regexAmount = "(^RSD[0-9]+\\.[0-9]?)$";
        Pattern patternAmount = Pattern.compile(regexAmount);
        Matcher matcherAmount = patternAmount.matcher(parts[5].split(":")[1]);
        boolean a = false;
        if(parts[5].split(":")[1].length()>=5 && parts[5].split(":")[1].length()<=18 && matcherAmount.matches()) {
            a = true;
        }

        String regexSF = "[0-9]{3}$";
        Pattern patternSF = Pattern.compile(regexSF);
        Matcher matcherSF = patternSF.matcher(parts[6].split(":")[1]);
        boolean sf = matcherSF.matches();

        if(matcherAccountNumber.matches() && matcherName.matches() && a && matcherSF.matches()) {
            return true;
        } else {
            return false;
        }
    }

}
