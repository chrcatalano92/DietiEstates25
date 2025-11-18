package com.demo.dietiestates25.support_classes;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomPwdGenerator {
    private RandomPwdGenerator(){}

    private static final int MIN_LENGTH = 16;
    private static final int MAX_LENGTH = 20;
    private static final int MIN_SPECIAL_CHARS = 2;
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuwxyz";
    private static final String UPPERCASE_LETTERS = LOWERCASE_LETTERS.toUpperCase();
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_{}|:.;,<>?+=";
    private static final SecureRandom random = new SecureRandom();

    public static String secureDefaultPwd() {
        int pwdLength = getRandomLength();

        List<Character> pwdChars = new ArrayList<>();

        addRequiredChars(pwdChars);

        remainingChars(pwdChars, pwdLength);

        Collections.shuffle(pwdChars);

        return convertListToString(pwdChars);
    }

    private static int getRandomLength() {
        return MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH + 1);
    }

    private static char getRandomChar(String charSet) {
        return charSet.charAt(random.nextInt(charSet.length()));
    }

    private static void addRequiredChars(List<Character> pwdChars) {
        pwdChars.add(getRandomChar(LOWERCASE_LETTERS));

        pwdChars.add(getRandomChar(UPPERCASE_LETTERS));

        pwdChars.add(getRandomChar(NUMBERS));

        for(int i=0; i < MIN_SPECIAL_CHARS; i++) {
            pwdChars.add(getRandomChar(SPECIAL_CHARS));
        }
    }

    private static void remainingChars(List<Character> pwdChars, int maxLengthPwd) {
        String allAllowedChars = LOWERCASE_LETTERS + UPPERCASE_LETTERS + NUMBERS + SPECIAL_CHARS;

        while(pwdChars.size() < maxLengthPwd) {
            pwdChars.add(getRandomChar(allAllowedChars));
        }
    }

    private static String convertListToString(List<Character> chars) {
        StringBuilder sb = new StringBuilder();

        for(char c : chars) {
            sb.append(c);
        }

        return sb.toString();
    }
}
