package com.example.yang.meetyou.utils;

import java.util.regex.Pattern;

/**
 * Created by Sinchuck on 16/10/18.
 */

public final class RegexUtils {

    public static boolean checkAccount(String account) {
        String regex = "20[0-9]{10}";
        return Pattern.matches(regex, account);
    }

    public static boolean checkPassword(String password) {
        String regex = "[a-zA-Z0-9]{6,20}";
        return Pattern.matches(regex, password);
    }

    public static boolean checkNickname(String nickname) {
        String regex = "^[a-zA-Z0-9\u4E00-\u9FA5_]+$";
        return Pattern.matches(regex, nickname);
    }
}
