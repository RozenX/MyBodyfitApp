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
        String minutes = time.substring(time.indexOf(":") + 1);
        try {
            return Integer.parseInt(minutes);
        } catch (Exception e) {
            if (minutes.equals("00")) {
                return 0;
            }
            if (minutes.indexOf("0") == 0) {
                return Integer.parseInt(Character.toString(minutes.charAt(1)));
            } else {
                return Integer.parseInt(minutes);
            }
        }
    }
}
