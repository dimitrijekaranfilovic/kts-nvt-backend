package com.ktsnvt.ktsnvt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KtsNvtApplication {

    public static void main(String[] args) {
        SpringApplication.run(KtsNvtApplication.class, args);
    }

}
