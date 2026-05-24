package com.vehiclerental.shared;

public class FieldCodec {
    private static final String DELIMITER = ",";
    private static final String ESCAPE    = "\\,";

    public static String encode(String value) {
        if (value == null) return "";
        return value.replace(DELIMITER, ESCAPE);
    }

    public static String decode(String value) {
        if (value == null) return "";
        return value.replace(ESCAPE, DELIMITER);
    }
}
