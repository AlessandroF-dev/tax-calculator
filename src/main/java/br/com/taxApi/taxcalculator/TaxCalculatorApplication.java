package br.com.taxApi.taxcalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableCaching
@EnableEurekaClient

public class TaxCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaxCalculatorApplication.class, args);
    }
}
