package com.qyl.mall;

import com.qyl.mall.utils.component.IdWorker;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.qyl.mall.mapper")
@EnableScheduling
public class MiMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiMallApplication.class, args);
    }

    @Bean
    public IdWorker getIdWork() {
        return new IdWorker();
    }

    @Bean
    public Queue queue() {
        return new Queue("seckill_order", true);
    }
}
