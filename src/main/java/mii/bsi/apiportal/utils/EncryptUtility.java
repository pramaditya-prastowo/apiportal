package mii.bsi.apiportal.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;

@Component
public class EncryptUtility {

    public boolean matchingPassword (String str1, String str2){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return  bCryptPasswordEncoder.matches(str1, str2);
    }

    public boolean matchingData(String value1, String value2){
        return value1.equals(value2);
    }

    public String encryptAES(String keyword, String passphrase) {
        try {
            String passwordKey = encryptMD5(passphrase).substring(0, 16);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            SecretKeySpec key = new SecretKeySpec(passwordKey.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(1, key, new IvParameterSpec(passwordKey.getBytes(StandardCharsets.UTF_8)));
            byte[] byteData = cipher.doFinal(keyword.getBytes(StandardCharsets.UTF_8));
            return byteToString(byteData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decryptAES(String keyword, String passphrase) {
        try {
            String passwordKey = encryptMD5(passphrase).substring(0, 16);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
            SecretKeySpec key = new SecretKeySpec(passwordKey.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(2, key, new IvParameterSpec(passwordKey.getBytes(StandardCharsets.UTF_8)));
            byte[] byteData = cipher.doFinal(hexStringToByteArray(keyword));
            return new String(byteData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    public String encryptMD5(String word) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(word.getBytes());
        byte[] byteData = md.digest();
        return byteToString(byteData);
    }

    public String byteToString(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(0xFF & data[i]);
            if (hex.length() == 1)
                sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    public String generateSignature(String prvKey, String message){
        //code/
        String SHA256RSA = "";
        byte[] keyBytes = Base64.getMimeDecoder().decode(prvKey.replaceAll("\\s","").getBytes(StandardCharsets.UTF_8));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");

            Signature privateSign = Signature.getInstance("SHA256withRSA");
            privateSign.initSign(fact.generatePrivate(keySpec));
            privateSign.update(message.getBytes(StandardCharsets.UTF_8));

            byte[] signs = privateSign.sign();
            SHA256RSA = Base64.getUrlEncoder().withoutPadding().encodeToString(signs);

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            SHA256RSA = "catch1";
        } catch (SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            SHA256RSA = "catch2";
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            SHA256RSA = "catch3";
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            SHA256RSA = "catch4";
        }
        return  SHA256RSA;
    }
}
