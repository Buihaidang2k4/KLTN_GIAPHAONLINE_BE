package com.codewithdang.kltn_giaphaonline.utils;

import java.time.LocalDate;

/**
 * Chuyển đổi âm lịch <-> dương lịch theo thuật toán Ho Ngoc Duc
 * Ref: https://www.informatik.uni-leipzig.de/~duc/amlich/calrules.html
 */
public class LunarSolarConverter {

    private static final double TIME_ZONE = 7.0;

    /**
     * Chuyển ngày âm lịch sang dương lịch
     *
     * @param lunarDay   ngày âm (1-30)
     * @param lunarMonth tháng âm (1-12)
     * @param lunarYear  năm âm (= năm dương xấp xỉ)
     * @param isLeap     có phải tháng nhuận không
     */
    public static LocalDate lunarToSolar(int lunarDay, int lunarMonth, int lunarYear, boolean isLeap) {
        int lunarMonth11Prev = getLunarMonth11(lunarYear - 1);
        int lunarMonth11Curr = getLunarMonth11(lunarYear);

        int k = (int) Math.floor((lunarMonth11Prev - 2415021.076998695) / 29.530588853);

        int leapOff = getLeapMonthOffset(lunarMonth11Prev);
        boolean hasLeap = leapOff < 13;

        // số tháng từ tháng 11 âm năm trước đến tháng lunarMonth
        // tháng 11 = 0, tháng 12 = 1, tháng 1 = 2, tháng 2 = 3, ...
        int off;
        if (lunarMonth >= 11) {
            off = lunarMonth - 11;
        } else {
            off = lunarMonth + 1; // tháng 1→2, tháng 2→3, ..., tháng 10→11
        }

        if (hasLeap && leapOff <= off) {
            off++;
        }
        if (isLeap) off++;

        int jd = getNewMoonDay(k + off) + lunarDay - 1;
        return jdToDate(jd);
    }

    // ==================== private helpers ====================

    private static int jdFromDate(int dd, int mm, int yy) {
        int a = (14 - mm) / 12;
        int y = yy + 4800 - a;
        int m = mm + 12 * a - 3;
        return dd + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045;
    }

    private static LocalDate jdToDate(int jd) {
        int a = jd + 32044;
        int b = (4 * a + 3) / 146097;
        int c = a - (b * 146097) / 4;
        int d = (4 * c + 3) / 1461;
        int e = c - (1461 * d) / 4;
        int m = (5 * e + 2) / 153;
        int day   = e - (153 * m + 2) / 5 + 1;
        int month = m + 3 - 12 * (m / 10);
        int year  = b * 100 + d - 4800 + m / 10;
        return LocalDate.of(year, month, day);
    }

    private static int getNewMoonDay(int k) {
        double T  = k / 1236.85;
        double T2 = T * T;
        double T3 = T2 * T;
        double jd = 2415020.75933
                + 29.53058868 * k
                + 0.0001178 * T2
                - 0.000000155 * T3
                + 0.00033 * Math.sin(Math.toRadians(166.56 + 132.87 * T - 0.009173 * T2));
        double M  = 359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347 * T3;
        double Mpr = 306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236 * T3;
        double F  = 21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239 * T3;
        double C1 = (0.1734 - 0.000393 * T) * Math.sin(Math.toRadians(M))
                + 0.0021 * Math.sin(Math.toRadians(2 * M))
                - 0.4068 * Math.sin(Math.toRadians(Mpr))
                + 0.0161 * Math.sin(Math.toRadians(2 * Mpr))
                - 0.0004 * Math.sin(Math.toRadians(3 * Mpr))
                + 0.0104 * Math.sin(Math.toRadians(2 * F))
                - 0.0051 * Math.sin(Math.toRadians(M + Mpr))
                - 0.0074 * Math.sin(Math.toRadians(M - Mpr))
                + 0.0004 * Math.sin(Math.toRadians(2 * F + M))
                - 0.0004 * Math.sin(Math.toRadians(2 * F - M))
                - 0.0006 * Math.sin(Math.toRadians(2 * F + Mpr))
                + 0.0010 * Math.sin(Math.toRadians(2 * F - Mpr))
                + 0.0005 * Math.sin(Math.toRadians(M + 2 * Mpr));
        double delta = (T < -11)
                ? 0.001 + 0.000839 * T + 0.0002261 * T2 - 0.00000845 * T3 - 0.000000081 * T * T3
                : -0.000278 + 0.000265 * T + 0.000262 * T2;
        return (int) (jd + C1 - delta + 0.5 + TIME_ZONE / 24.0);
    }

    private static int getSunLongitude(int jdn) {
        double T  = (jdn - 2451545.5 - TIME_ZONE / 24.0) / 36525;
        double T2 = T * T;
        double dr = Math.PI / 180;
        double M  = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2;
        double L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2;
        double DL = (1.914600 - 0.004817 * T - 0.000014 * T2) * Math.sin(dr * M)
                + (0.019993 - 0.000101 * T) * Math.sin(dr * 2 * M)
                + 0.000290 * Math.sin(dr * 3 * M);
        double L  = L0 + DL;
        L = L * dr;
        L = L - Math.PI * 2 * Math.floor(L / (Math.PI * 2));
        return (int) (L / Math.PI * 6);
    }

    private static int getLunarMonth11(int year) {
        int off = jdFromDate(31, 12, year) - 2415021;
        int k   = (int) Math.floor((double) off / 29.530588853);
        int nm  = getNewMoonDay(k);
        int sunLong = getSunLongitude(nm);
        if (sunLong >= 9) {
            nm = getNewMoonDay(k - 1);
        }
        return nm;
    }

    private static int getLeapMonthOffset(int a11) {
        int k   = (int) Math.floor((a11 - 2415021.076998695) / 29.530588853);
        int last;
        int i   = 1;
        int arc = getSunLongitude(getNewMoonDay(k + i));
        do {
            last = arc;
            i++;
            arc = getSunLongitude(getNewMoonDay(k + i));
        } while (arc != last && i < 14);
        return i - 1;
    }
}
