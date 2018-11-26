package net.hotsmc.practice.utility;

public class NumberUtility {

    public static boolean isInteger(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException nfex) {
            return false;
        }
    }

    public static boolean isDouble(String val) {
        try {
            Double.parseDouble(val);
            return true;
        } catch (NumberFormatException nfex) {
            return false;
        }
    }
}
