package com.taimoor.wallpixels;

public class Utils {

    public static String convertToKilo(long number){
        String kilo;
        if (number>=1000) {
            kilo = String.format("%.2fK", number / 1000.0);
        }else {
            kilo = String.valueOf(number);
        }

        return kilo;
    }


}
