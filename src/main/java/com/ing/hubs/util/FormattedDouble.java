package com.ing.hubs.util;

import java.text.DecimalFormat;

public class FormattedDouble {
    public static Double getFormattedDouble(double number){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedString = decimalFormat.format(number);
        double formattedDouble = Double.parseDouble(formattedString);
        return formattedDouble;
    }
}
