package com.mohamed.tamer.ch11_authentication_server.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class GenerateCodeUtil {
    private GenerateCodeUtil() {}

    public static String generateCode() {
        String code;

        try{
            SecureRandom random = SecureRandom.getInstanceStrong();
            int number = random.nextInt(9000) + 1000;
            code = String.valueOf(number);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem when generating the random code");
        }

        return code;
    }
}
