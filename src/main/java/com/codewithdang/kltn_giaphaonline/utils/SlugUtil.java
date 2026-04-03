package com.codewithdang.kltn_giaphaonline.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtil {

    // Tìm tất cả các ký tự không phải là chữ cái, chữ số, hoặc dấu gạch ngang.
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    // Tìm các khoảng trắng (dấu cách, tab, xuống dòng).
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");

    private SlugUtil() {
    }

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }

        String noAccent = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace('đ', 'd')
                .replace('Đ', 'D');

        String slug = WHITESPACE.matcher(noAccent.trim()).replaceAll("-");
        slug = NONLATIN.matcher(slug).replaceAll("");
        slug = slug.replaceAll("-{2,}", "-");

        return slug.toLowerCase(Locale.ROOT);
    }
}