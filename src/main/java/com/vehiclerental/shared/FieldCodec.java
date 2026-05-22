package com.vehiclerental.shared;

/**
 * Encodes/decodes user-supplied strings for safe comma-delimited storage.
 * Commas → \c  |  backslashes → \\  so parsing is never ambiguous.
 */
public class FieldCodec {

    public static String encode(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace(",", "\\c");
    }

    public static String decode(String value) {
        if (value == null) return "";
        return value.replace("\\c", ",").replace("\\\\", "\\");
    }
}
