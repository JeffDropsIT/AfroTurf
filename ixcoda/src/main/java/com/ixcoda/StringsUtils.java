package com.ixcoda;

public class StringsUtils {

    public static String padStart(String str, int length, char delim){
        if(str.length() >= length)
            return str;

        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < length)
            stringBuilder.append(delim);

        return stringBuilder.toString();
    }
}
