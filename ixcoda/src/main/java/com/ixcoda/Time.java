package com.ixcoda;

/**
 * Created by ASANDA on 2018/10/06.
 * for Pandaphic
 */
public class Time {
    public final int Hour, Min, Sec;

    public Time(int hour, int min, int sec){
        this.Hour = hour;
        this.Min = min;
        this.Sec = sec;
    }

    @Override
    public String toString() {
        return padRight(this.Hour + "", 2, '0') + ":" + padRight(this.Min +  "", 2, '0')  + ":" + padRight(this.Sec + "", 2, '0');
    }

    String padRight(String str, int len, char delim){
        if(str.length() >= len)
            return str;
        else{
            return padRight(delim + str, len, delim);
        }
    }
}
