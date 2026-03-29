package com.codewithdang.kltn_giaphaonline.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

public class HtmlUtils {

    /**
     * Làm sạch HTML để chống tấn công XSS
     */
    public static String sanitize(String html) {
        if (html == null) return null;
        // Cho phép các thẻ định dạng văn bản, ảnh và link
        return Jsoup.clean(html, Safelist.relaxed());
    }

    /**
     * Chuyển HTML thành text thuần để làm tóm tắt
     */
    public static String toPlainText(String html) {
        if (html == null) return "";
        return Jsoup.parse(html).text();
    }

    /**
     * Lấy URL của hình ảnh đầu tiên trong bài viết
     */
    public static String getFirstImageUrl(String html) {
        Document doc = Jsoup.parse(html);
        return doc.select("img").attr("src");
    }
}