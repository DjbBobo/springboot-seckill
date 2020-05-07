package com.bo.utils;

import java.util.Random;

public class CodeUtil {
    public static String getCheckCode(int count){
        Random random = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < count; i++) {
            str.append(random.nextInt(10));
        }
        return String.valueOf(str);
    }
}
