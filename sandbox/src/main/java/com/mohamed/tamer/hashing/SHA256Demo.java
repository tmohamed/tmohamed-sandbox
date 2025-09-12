package com.mohamed.tamer.hashing;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class SHA256Demo {
    public static void main(String[] args) throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        byte [] code = new byte[32];
        secureRandom.nextBytes(code);
        String codeVerifier = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(code);

        System.out.println("codeVerifier = " + codeVerifier);

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte [] digested = messageDigest.digest(codeVerifier.getBytes());
        String codeChallenge = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(digested);

        System.out.println("codeChallenge = " + codeChallenge);
    }
}
