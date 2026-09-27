package com.re4n.internalhub.util;

import java.security.SecureRandom;

public class CredentialGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String EMPLOYEE_ID_CHARS = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private static final int EMPLOYEE_ID_LENGTH = 6;
    private static final int TEMPORARY_PASSWORD_LENGTH = 12;
    private static final String PASSWORD_CHARS = "ABCDEFGHJKMNPQRSTUVWXYZ23456789abcdefghjkmnpqrstuvwxyz#$@*!-";

    public String genEmployeeId(){
        final StringBuilder sb = new StringBuilder("IH-");
        for (int i = 0; i < EMPLOYEE_ID_LENGTH; i++){
            int randomIndex = RANDOM.nextInt(EMPLOYEE_ID_CHARS.length());
            sb.append(EMPLOYEE_ID_CHARS.charAt(randomIndex));
        }
        return  sb.toString();
    }

    public String genTemporaryPassword(){
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < TEMPORARY_PASSWORD_LENGTH; i++){
            int randomIndex = RANDOM.nextInt(PASSWORD_CHARS.length());
            sb.append(PASSWORD_CHARS.charAt(randomIndex));
        }
        return sb.toString();
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
