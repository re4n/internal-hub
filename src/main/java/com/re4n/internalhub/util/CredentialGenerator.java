package com.re4n.internalhub.util;

import java.security.SecureRandom;

public class CredentialGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CHARS = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private static final int LENGTH = 6;

    public String genEmployeeId(){
        final StringBuilder sb = new StringBuilder("IH-");
        for (int i= 0; i < LENGTH; i++){
            int randomIndex = RANDOM.nextInt(CHARS.length());
            sb.append(CHARS.charAt(randomIndex));
        }
        return  sb.toString();
    }

    public String genEmployeeCorporateEmail(String firstName, String lastName, int attempt){
        char firstLetter = firstName.toLowerCase().charAt(0);
        String baseEmail = firstLetter + lastName.toLowerCase();
        String localCorporateEmail;
        if (attempt == 0){
            localCorporateEmail = baseEmail;
        }else{
            localCorporateEmail = baseEmail + attempt;
        }
        return localCorporateEmail + "@internalhub.com";
    }
}
