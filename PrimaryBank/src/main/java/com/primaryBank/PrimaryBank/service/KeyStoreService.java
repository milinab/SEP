package com.primaryBank.PrimaryBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class KeyStoreService {

    @Value("${keystore.path}")
    private String KEYSTORE_PATH;

    @Value("${keystore.password}")
    private String KEYSTORE_PASSWORD;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final byte[] IV_BYTES = {
            (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04,
            (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08,
            (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C,
            (byte) 0x0D, (byte) 0x0E, (byte) 0x0F, (byte) 0x10
    };
    private static final IvParameterSpec IV = new IvParameterSpec(IV_BYTES);

    public void addKey(String alias, String keyPassword, SecretKey secretKey) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            File keystoreFile = new File(KEYSTORE_PATH);

            if (keystoreFile.exists()) {
                FileInputStream fis = new FileInputStream(keystoreFile);
                keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
                fis.close();
            } else {
                createNewKeystore();
                FileInputStream fis = new FileInputStream(keystoreFile);
                keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
                fis.close();
            }

            KeyStore.SecretKeyEntry secret= new KeyStore.SecretKeyEntry(secretKey);
            KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(keyPassword.toCharArray());
            keyStore.setEntry(alias, secret, password);

            FileOutputStream fos = new FileOutputStream(KEYSTORE_PATH);
            keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
            fos.close();

            System.out.println("Tajni ključ je uspješno dodan u keystore.");
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createNewKeystore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JCEKS");

            keyStore.load(null, KEYSTORE_PASSWORD.toCharArray());

            FileOutputStream fos = new FileOutputStream(KEYSTORE_PATH);
            keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
            fos.close();

            System.out.println("Kreiran novi keystore.");
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        return secretKey;
    }

    public String encrypt(String plaintext, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IV);

        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IV);

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public SecretKey getKey(String alias, String keyPassword) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            File keystoreFile = new File(KEYSTORE_PATH);

            if (keystoreFile.exists()) {
                FileInputStream fis = new FileInputStream(keystoreFile);
                keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
                fis.close();
            } else {
                System.out.println("Keystore file does not exist.");
                return null;
            }

            KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(keyPassword.toCharArray());
            KeyStore.Entry entry = keyStore.getEntry(alias, entryPassword);

            if (entry == null) {
                System.out.println("Entry with alias '" + alias + "' does not exist in the keystore.");
                return null;
            }

            if (!(entry instanceof KeyStore.SecretKeyEntry)) {
                System.out.println("Entry with alias '" + alias + "' is not a SecretKey entry.");
                return null;
            }

            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) entry;
            return secretKeyEntry.getSecretKey();
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException |
                 UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
