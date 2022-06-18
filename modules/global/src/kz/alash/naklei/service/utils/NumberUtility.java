package kz.alash.naklei.service.utils;

import java.text.DecimalFormat;

public class NumberUtility {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static double formatDouble(double input) {
        return Math.round(input * 100.0) / 100.0;
    }
}
