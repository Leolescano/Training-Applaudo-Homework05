package com.applaudo.homework5.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {

  public static boolean emailValidator(String email) {
    Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    Matcher matcher = pattern.matcher(email);

    return matcher.matches();
  }

  public static boolean phoneNumberValidator(String phone) {
    Pattern pattern = Pattern.compile("^\\+503 \\d{4} \\d{4}$");
    Matcher matcher = pattern.matcher(phone);

    return matcher.matches();
  }
}
