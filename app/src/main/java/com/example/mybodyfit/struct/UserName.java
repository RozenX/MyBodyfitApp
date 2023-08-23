package com.example.mybodyfit.struct;

public class UserName {

    public static String getName(String email) {
        return email.substring(0, email.indexOf('@'));
    }
}
