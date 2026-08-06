package com.re4n.internalhub;

import com.re4n.internalhub.util.CredentialGenerator;

public class Main {
    public static void main(String[] args) {
        CredentialGenerator generator = new CredentialGenerator();
        String randomNumber = generator.genEmployeeId();

        System.out.println(randomNumber);

        String randomEmail =  generator.genEmployeeCorporateEmail("Lucas", "Silva");

        System.out.println(randomEmail);

        }



    }