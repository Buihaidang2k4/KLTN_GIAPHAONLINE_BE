package com.codewithdang.kltn_giaphaonline;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class KltnGiaphaonlineApplication {
    public static void main(String[] args) {
        SpringApplication.run(KltnGiaphaonlineApplication.class, args);
    }

}
