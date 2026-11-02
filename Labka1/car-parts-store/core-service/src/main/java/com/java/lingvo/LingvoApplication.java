package com.java.lingvo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;

@SpringBootApplication(exclude = {QuartzAutoConfiguration.class})
public class LingvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LingvoApplication.class, args);
    }

}
