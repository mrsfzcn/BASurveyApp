package com.bilgeadam.basurveyapp.utilty;

import java.util.UUID;

public class Helpers {
    public static String handleTurkish(String input) {
        char [] turkishLetter= {'İ', 'ı', 'ş', 'Ş', 'ç', 'Ç', 'ğ', 'Ğ', 'ü','Ü','ö','Ö'};
        char [] englishLetter= {'I', 'i', 's', 'S', 'c', 'C', 'g', 'G', 'u','U','o','O'};
        for (int count = 0; count < englishLetter.length; count++) {
            input=input.replace(turkishLetter[count], englishLetter[count]);
        }
        return input;
    }

    public static String generatePassword(){
        String code = UUID.randomUUID().toString();
        String[] data = code.split("-");
        StringBuilder newCode = new StringBuilder();
        for (String string : data) {
            newCode.append(string.charAt(0));
        }
        return newCode.toString();
    }


}
