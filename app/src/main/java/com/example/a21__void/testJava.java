package com.example.a21__void;

import android.provider.Settings;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ASANDA on 2018/12/05.
 * for Pandaphic
 */
public class testJava {

    static ArrayList<Integer> integers = new ArrayList<>();

    public static void main(String[] args){
        int c = 100000;
        for(int pos = 0; pos < c; pos++)
            integers.add(pos);

        Scanner scanner = new Scanner(System.in);
        String line = scanner.next();
        System.out.println(line);
    }

}
