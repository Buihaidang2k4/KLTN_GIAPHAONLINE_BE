package com.codewithdang.kltn_giaphaonline.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyStatRes {
    String month;        // "2026-01", "2026-02"...
    Long count;          // số lượng (account mới, family mới...)
    BigDecimal value;    // giá trị tiền (doanh thu)
}
