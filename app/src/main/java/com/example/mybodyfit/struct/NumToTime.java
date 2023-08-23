package com.example.mybodyfit.struct;

public class NumToTime {

    public static String getTime(int hour, int min) {
        if (min < 10)
            return hour + ":" + "0" + min;
        return hour + ":" + min;
    }

    public static int getHours(String time) {
        return Integer.parseInt(time.substring(0, time.indexOf(":")));
    }

    public static int getMinutes(String time) {
        return Integer.parseInt(time.substring(time.indexOf(":")));
    }
}
