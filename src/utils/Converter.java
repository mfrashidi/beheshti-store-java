package utils;

import java.util.regex.Pattern;

public class Converter {
    static char[] persianChars = new char[] {'آ', 'ا', 'ب', 'پ', 'ت', 'ث', 'ج', 'چ', 'ح', 'خ', 'د', 'ذ', 'ر', 'ز', 'ژ', 'س', 'ش', 'ص', 'ض', 'ط', 'ظ', 'ع', 'غ', 'ف', 'ق', 'ک', 'گ', 'ل', 'م', 'ن', 'و', 'ه', 'ی', '‌', '۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹'};

    public static String unNormalize(String text) {
        for (int i = 0; i < persianChars.length; i++) {
            text = text.replaceAll(String.valueOf(persianChars[i]), "." + i + ".");
        }
        return text;
    }

    public static String normalize(String text) {
        for (int i = 0; i < persianChars.length; i++) {
            text = text.replaceAll(Pattern.quote("." + i + "."), String.valueOf(persianChars[i]));
        }
        return text;
    }
}

