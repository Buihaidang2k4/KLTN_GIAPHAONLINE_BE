package com.codewithdang.kltn_giaphaonline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentProvider implements BaseEnum {
    VNPAY("Cổng thanh toán VNPAY");

    private final String label;

    @Override
    public String getLabel() {
        return this.label;
    }
}