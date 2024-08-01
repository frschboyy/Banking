package com.example.banking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** @noinspection ALL*/
public class credentialVal {
    private String name;
    private String phone;
    private final String email;
    private final String password;
    private String confirm;

    public credentialVal(String mail, String pass){
        email = mail;
        password = pass;
    }

    public credentialVal(String name, String phone, String mail, String pass, String confirm){
        this.name = name;
        this.phone = phone;
        email = mail;
        password = pass;
        this.confirm = confirm;
    }

    public boolean validName(){
        for(char ch : name.toCharArray()){
            if(Character.isDigit(ch)){
                return false;
            }
            else if(isSpecialChar(ch)) {
                if (ch == ' ') {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    public boolean validConfirm(){
        // check if password in confirmed correctly
        return password.equals(confirm);
    }

    public boolean validEmail() {
        // regular expression for validating email address
        String emailRegEx = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        Pattern pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validPassword() {
        // check password length
        if (password.length() < 8) {
            return false;
        }

        // check for other conditions
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasSpecialChar = false;
        boolean hasDigit = false;

        for(char ch : password.toCharArray()){
            if(Character.isUpperCase(ch)){
                hasUppercase = true;
            }
            else if (Character.isLowerCase(ch)){
                hasLowercase = true;
            }
            else if (Character.isDigit(ch)){
                hasDigit = true;
            }
            else if (isSpecialChar(ch)){
                hasSpecialChar = true;
            }
        }
        // all conditions must hold true
        return hasUppercase && hasLowercase && hasSpecialChar && hasDigit;
    }

    public boolean isSpecialChar(char ch){
        //Define set of special characters
        String special = "!@#$%^&*()-_=+[]{};:'\"\\|,.<>/? ";
        return special.contains(String.valueOf(ch));
    }
}
