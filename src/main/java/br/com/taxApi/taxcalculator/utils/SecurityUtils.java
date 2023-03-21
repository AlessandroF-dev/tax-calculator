package br.com.taxApi.taxcalculator.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.regex.Pattern;

public class SecurityUtils {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$*%^&+=])(?=\\S+$).{8,16}$";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    public static String encryptPassword(String password) {
        String generatedSalt = BCrypt.gensalt();
        return BCrypt.hashpw(password, generatedSalt);
    }

    public static boolean validPassword(String password) {
        if (PASSWORD_PATTERN.matcher(password).matches()) {
            return true;
        }
        return false;
    }

    public static String mountMessage(String name, double salary, double taxValue) {
        return "Olá " + name
                + ", o valor do imposto de renda a partir do sálario informado de R$" + salary +
                " é: R$" + taxValue;
    }
}
