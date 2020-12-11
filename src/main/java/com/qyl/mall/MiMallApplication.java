package com.qyl.mall;

import com.qyl.mall.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MiMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiMallApplication.class, args);
    }

    @Bean
    public IdWorker getIdWork() {
        return new IdWorker();
    }

}
